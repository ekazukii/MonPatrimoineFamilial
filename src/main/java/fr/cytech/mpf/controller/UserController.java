package fr.cytech.mpf.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.LoginDTO;
import fr.cytech.mpf.dto.RegisterDTO;
import fr.cytech.mpf.dto.UserAddDTO;
import fr.cytech.mpf.dto.UserGetDTO;
import fr.cytech.mpf.entity.Node;
import fr.cytech.mpf.entity.Tree;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.function.EntityResponse;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

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
    NodeRepository nodeRepository;
    ModelMapper modelMapper;

    UserController() {
        modelMapper = new ModelMapper();
    }

    @CrossOrigin
    @PostMapping(value = "/user")
    public void addUser(@RequestBody UserAddDTO userDto) {
        User user = modelMapper.map(userDto, User.class);
        userRepository.save(user);
    }

    @CrossOrigin
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUser() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

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

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestParam String personalInfo,
                                                @RequestParam("carteIdentite") MultipartFile carteIdentite,
                                                @RequestParam("photo") MultipartFile photo, HttpSession session) throws NoSuchAlgorithmException, IOException, RuntimeException{

        ObjectMapper objectMapper = new ObjectMapper();
        RegisterDTO personalInfoData = objectMapper.readValue(personalInfo, RegisterDTO.class);

        userService.saveFileImage(carteIdentite, personalInfoData.getUsername(), "idcard");
        userService.saveFileImage(photo, personalInfoData.getUsername(), "photo");

        Tree tree = new Tree();
        tree.setName("Arbre de " + personalInfoData.getLastName());
        treeRepository.save(tree);

        User user = modelMapper.map(personalInfoData, User.class);
        user.setTree(tree);
        user.setPassword(userService.passwordToHash(personalInfoData.getPassword()));
        UUID validationCode = UUID.randomUUID();
        user.setValidationCode(validationCode);
        userRepository.save(user);

        mailService.sendValidationCode(user);

        Node rootNode = new Node(personalInfoData.getFirstName(), personalInfoData.getLastName(), personalInfoData.getBirthDate(), NodeVisibility.Private, tree, personalInfoData.isMale(), user);
        nodeRepository.save(rootNode);

        //TODO: Send validation email
        System.out.println("[USER] - Registering new user "+ personalInfoData.getEmail());

        return ResponseEntity.ok(user);
    }

    @GetMapping("/user/validate")
    public String validateUser(@RequestParam String code) {
        List<User> users = userRepository.findUsersByValidationCode(UUID.fromString(code));
        if (users != null && !users.isEmpty()) {
            // Des utilisateurs ont été trouvés avec le code de validation
            users.forEach((u) -> u.setValidationCode(null));
            userRepository.saveAll(users);
            //TODO: Mettre une route vers un message de validation
            return "redirect:http://localhost:5173/login";
        } else {
            // Aucun utilisateur n'a été trouvé avec le code de validation
            //TODO: Mettre une route vers un message d'erreur (non trouvé)
            return "redirect:http://localhost:5173/";
        }
    }

    @CrossOrigin
    @GetMapping("/userinfo")
    public ResponseEntity<User> getUserInfo(HttpSession session) {
        User usr = (User) session.getAttribute("account");
        System.out.println(usr);
        return ResponseEntity.ok(usr);
    }

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
}
