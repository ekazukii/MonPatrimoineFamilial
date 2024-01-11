package fr.cytech.mpf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.*;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.TreeView;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.TreeViewRepository;
import fr.cytech.mpf.repository.UserRepository;
import fr.cytech.mpf.service.MailService;
import fr.cytech.mpf.service.UserService;
import fr.cytech.mpf.utils.NodeVisibility;
import jakarta.servlet.http.HttpSession;
import jdk.jshell.spi.ExecutionControlProvider;
import lombok.SneakyThrows;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

/**
 * MVC Controller for the tree node features
 */
@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    TreeRepository treeRepository;
    @Autowired
    TreeViewRepository treeViewRepository;
    @Autowired
    NodeRepository nodeRepository;
    ModelMapper modelMapper;

    @Value("${front.url:http://localhost:5173}")
    String frontUrl;

    @Value("${root.path:/Desktop/}")
    String rootLocationString;

    UserController() {
        modelMapper = new ModelMapper();
    }

    /**
     * Create a new user without registering
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param userDto
     * @deprecated
     */
    @CrossOrigin
    @PostMapping(value = "/user")
    public void addUser(@RequestBody UserAddDTO userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    /**
     * Get list of all users
     * HTTP Code 400 if body is malformed 200 otherwise
     * @return
     */
    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    @CrossOrigin
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Server is up and running");
    }

    /**
     * Login to the application
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param loginDTO Login information object
     * @param session current user session
     * @return User
     * @throws NoSuchAlgorithmException
     */
    @CrossOrigin
    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginDTO loginDTO, HttpSession session) throws NoSuchAlgorithmException {
        String hashedPass = userService.passwordToHash(loginDTO.getPassword());
        Optional<User> optUser = userRepository.findUserByUsernameAndPassword(loginDTO.getUsername(), hashedPass);
        if(optUser.isEmpty()) return ResponseEntity.notFound().build();
        User user = optUser.get();
        if(user.getValidationCode() != null) return ResponseEntity.status(403).build();
        session.setAttribute("account", user);
        return ResponseEntity.ok(user);
    }
    private final Path rootLocation = Paths.get(rootLocationString);
  
    /**
     * Register a new user in the application
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param personalInfo RegisterDTO - Information of the user
     * @param carteIdentite Image of the idCard
     * @param photo Image of the user
     * @param session current session of the user
     * @return User
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws RuntimeException
     */
    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestParam String personalInfo,
                                                @RequestParam("carteIdentite") MultipartFile carteIdentite,
                                                @RequestParam("photo") MultipartFile photo, HttpSession session) throws NoSuchAlgorithmException, IOException, RuntimeException{

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterDTO personalInfoData = objectMapper.readValue(personalInfo, RegisterDTO.class);

        userService.generateUniqueUsername(personalInfoData);

        // Vérification de la date de naissance
        if (!userService.isBirthdateValid(personalInfoData.getBirthDate())) {
            return ResponseEntity.badRequest().body(null);
        }


        userService.saveFileImage(carteIdentite,  rootLocationString, personalInfoData.getUsername(), "idcard");
        userService.saveFileImage(photo, rootLocationString, personalInfoData.getUsername(), "photo");

        Tree tree = new Tree();
        tree.setName("Arbre de " + personalInfoData.getLastName());
        treeRepository.save(tree);

        User user = modelMapper.map(personalInfoData, User.class);
        user.setTree(tree);
        user.setPassword(userService.passwordToHash(personalInfoData.getPassword()));
        UUID validationCode = UUID.randomUUID();
        user.setValidationCode(validationCode);
        userRepository.save(user);

        tree.setOwner(user);

        mailService.sendValidationCode(user);

        // Convert birthdate from YYYY-MM-DD to DD/MM/YYYY
        String[] birthdateParts = personalInfoData.getBirthDate().split("-");
        String birthdate = birthdateParts[2] + "/" + birthdateParts[1] + "/" + birthdateParts[0];

        Node rootNode = new Node(personalInfoData.getFirstName(), personalInfoData.getLastName(), birthdate, NodeVisibility.Private, tree, personalInfoData.isMale(), user);
        nodeRepository.save(rootNode);

        //TODO: Send validation email
        System.out.println("[USER] - Registering new user "+ personalInfoData.getEmail());

        return ResponseEntity.ok(user);
    }


    /**
     * Handles HTTP GET requests for serving image files.
     *
     * @param filename The name of the file to be served. It is extracted from the URL.
     * @return A {@link ResponseEntity} object containing the image file as a resource. 
     *         Returns the file with a 'Content-Disposition' header to prompt the browser 
     *         to download the file. If the file is not found or not readable, a 404 Not Found 
     *         response is returned. In case of any other exception, a 400 Bad Request response 
     *         is returned.
     */
    @GetMapping("/user/images/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Path file = rootLocation.resolve(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity
                    .ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity
                    .notFound()
                    .build();
            }
        } catch (Exception e) {
            return ResponseEntity
                .badRequest()
                .build();
        }
    }


    /**
     * Validate user account using secret code
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param code code that has been received in email
     * @return redirect to login or home page
     */
    @GetMapping("/user/validate")
    public String validateUser(@RequestParam String code) {
        List<User> users = userRepository.findUsersByValidationCode(UUID.fromString(code));
        if (users != null && !users.isEmpty()) {
            // Des utilisateurs ont été trouvés avec le code de validation
            users.forEach((u) -> u.setValidationCode(null));
            userRepository.saveAll(users);
            //TODO: Mettre une route vers un message de validation
            return "redirect:"+frontUrl+"/login";
        } else {
            // Aucun utilisateur n'a été trouvé avec le code de validation
            //TODO: Mettre une route vers un message d'erreur (non trouvé)
            return "redirect:"+frontUrl;
        }
    }

    /**
     * Search user by query on first, last or user name, can also add filter on gender and birthdate
     * HTTP Code 400 if body is malformed 200 otherwise
     *
     * @param query     String to compare with names of all users
     * @param gender    exact match of the user gender (optional)
     * @param birthdate exact match of the user birthdate (optional)
     * @return list of users
     */
    @GetMapping("/user/search")
    public ResponseEntity<List<User>> getUser(
            @RequestParam String query,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String birthdate) {

        List<User> users;
        Boolean genderValue = (gender != null) ? Boolean.valueOf(gender) : null;

        if (genderValue != null && birthdate != null) {
            // Both gender and birthdate are provided as filters
            users = userRepository.findByFirstnameLastnameOrUsernameAndGenderAndBirthdate(
                    query.toLowerCase(), genderValue, birthdate);
        } else if (genderValue != null) {
            // Only gender is provided as a filter
            users = userRepository.findByFirstnameLastnameOrUsernameAndGender(
                    query.toLowerCase(), genderValue);
        } else if (birthdate != null) {
            // Only birthdate is provided as a filter
            users = userRepository.findByFirstnameLastnameOrUsernameAndBirthdate(
                    query.toLowerCase(), birthdate);
        } else {
            // No filters
            users = userRepository.findByFirstnameLastnameOrUsernameContainingIgnoreCase(query.toLowerCase());
        }

        return ResponseEntity.ok(users);
    }

    /**
     * Get user information in the user session
     * @param session current session
     * @return User
     */
    @CrossOrigin
    @GetMapping("/userinfo")
    public ResponseEntity<User> getUserInfo(HttpSession session) {
        User usr = (User) session.getAttribute("account");
        System.out.println(usr);
        return ResponseEntity.ok(usr);
    }

    /**
     * Logout to the current session
     * @param session current session
     * @return "ok" if succesfully disconnected
     */
    @MustBeLogged
    @CrossOrigin
    @GetMapping("/logout") @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("ok");
    }

    @CrossOrigin
    @PostMapping(value = "/addtestuser")
    public ResponseEntity<String> addTestUser() {
        User user = new User();
        user.setEmail("baptisteloison8400@gmail.com");
        user.setFirstname("baptiste");
        user.setLastname("baptiste");
        user.setPassword("trqllemdpestpashashosef");
        user.setUsername("xX_D4rK_4Nat0l3_Xx");
        userRepository.save(user);
        return ResponseEntity.ok("ok");
    }

    @CrossOrigin
    @GetMapping(value = "/userrdm")
    public ResponseEntity<UserGetDTO> getUserRandom() {
        Optional<User> user = userRepository.findAll().stream().findFirst();
        if(user.isPresent()) {
            return ResponseEntity.ok(modelMapper.map(user.get(), UserGetDTO.class));
        }
        return ResponseEntity.internalServerError().build();
    }

    /**
     * Delete an user of the database
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param userDto id of the user to delete
     * @return "ok" if delete is successfull
     */
    @CrossOrigin
    @DeleteMapping("/user")
    public ResponseEntity<String> removeNode (@RequestBody UserDeleteDTO userDto) {
        List<Node> nodesWithUser = nodeRepository.findByUserAccountId(userDto.getUserId());
        Tree treeToDelete = treeRepository.findTreeByOwnerId(userDto.getUserId());
        List<Node> nodeToDelete = nodeRepository.findAllByTreeEquals(treeToDelete);
        List<TreeView> treeViewToDeletByIdTree = treeViewRepository.getAllByTreeId(treeToDelete.getId());
        List<TreeView> treeViewToDeleteByIdUser = treeViewRepository.getAllByViewerId(userDto.getUserId());

        for(Node node : nodeToDelete){
            node.setFather(null);
            node.setMother(null);
        }
        for(Node node : nodeToDelete){
            nodeRepository.deleteById(node.getId());
        }
        for(Node node : nodesWithUser){
            node.setUserAccount(null);
        }
        // Delete tree view
        treeViewRepository.deleteAll(treeViewToDeletByIdTree);
        treeViewRepository.deleteAll(treeViewToDeleteByIdUser);

        // To be aligned with BD constraint, we set Id reference to null
        treeToDelete.setOwner(null);
        userRepository.getById(userDto.getUserId()).setTree(null);
        // Delete tree
        treeRepository.delete(treeToDelete);
        // Delete user
        userRepository.deleteById(userDto.userId);
        // TODO: Check has permissions to edit
        return ResponseEntity.ok("ok");
    }

    /**
     * Edit user personnal information, if user is admin also edit validation status
     * HTTP Code 400 if body is malformed 200 otherwise
     * @param userEditDTO new user information
     * @return saved user
     */
    @MustBeLogged
    @CrossOrigin
    @PutMapping("/user")
    public ResponseEntity<?> editUser(@RequestBody UserEditDTO userEditDTO) {
        Optional<User> actualUser = userRepository.findById(userEditDTO.getId());
        if(actualUser.isEmpty()) return ResponseEntity.notFound().build();

        User userToUpdate = actualUser.get();
        System.out.println(userEditDTO.getNewPassword());

        // Mettre à jour les propriétés de l'utilisateur avec les valeurs du DTO
        if(userEditDTO.getUsername() != null) userToUpdate.setUsername(userEditDTO.getUsername());
        if(userEditDTO.getLastname() != null) userToUpdate.setLastname(userEditDTO.getLastname());
        if(userEditDTO.getFirstname() != null) userToUpdate.setFirstname(userEditDTO.getFirstname());
        if(userEditDTO.getIsMale() != null) userToUpdate.setMale(userEditDTO.getIsMale());
        if(userEditDTO.getEmail() != null) userToUpdate.setEmail(userEditDTO.getEmail());
        if(userEditDTO.getIsAdmin() != null && userEditDTO.getIsAdmin() != userToUpdate.isAdmin()) userToUpdate.setAdmin(userEditDTO.getIsAdmin());
        if(userEditDTO.getIsValidated() != null) {
            if(userEditDTO.getIsValidated() && userToUpdate.getValidationCode() != null) {
                userToUpdate.setValidationCode(null);
            } else if(!userEditDTO.getIsValidated() && userToUpdate.getValidationCode() == null) {
                userToUpdate.setValidationCode(UUID.randomUUID());
                mailService.sendValidationCode(userToUpdate);
            }
        }

        try {
            if ((!userEditDTO.getNewPassword().isEmpty() && !userEditDTO.getOldPassword().isEmpty()) && !userService.passwordToHash(userEditDTO.getOldPassword()).equals(userToUpdate.getPassword())){
                return ResponseEntity.badRequest().body("L'ancien mot de passe n'est pas correct");
            } else if ((!userEditDTO.getNewPassword().isEmpty() && !userEditDTO.getOldPassword().isEmpty()) && userService.passwordToHash(userEditDTO.getOldPassword()).equals(userToUpdate.getPassword())) {
                userToUpdate.setPassword(userService.passwordToHash(userEditDTO.getNewPassword()));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        userRepository.save(userToUpdate);
        return ResponseEntity.ok(userToUpdate);
    }
}
