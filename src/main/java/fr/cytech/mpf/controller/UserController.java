package fr.cytech.mpf.controller;

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
import fr.cytech.mpf.service.UserService;
import fr.cytech.mpf.utils.NodeVisibility;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;
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
        Optional<User> optUser = userRepository.findUserByUsernameAndPassword(loginDTO.getUsername(), userService.passwordToHash(loginDTO.getPassword()));
        if(optUser.isEmpty()) return ResponseEntity.notFound().build();
        User user = optUser.get();
        if(user.getValidationCode() != null) return ResponseEntity.status(403).build();
        session.setAttribute("account", user);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestBody RegisterDTO registerDTO, HttpSession session) throws NoSuchAlgorithmException {
        Tree tree = new Tree();
        tree.setName("Arbre de " + registerDTO.getLastname());
        treeRepository.save(tree);

        User user = modelMapper.map(registerDTO, User.class);
        user.setTree(tree);
        user.setPassword(userService.passwordToHash(registerDTO.getPassword()));
        UUID validationCode = UUID.randomUUID();
        user.setValidationCode(validationCode);

        Node rootNode = new Node(registerDTO.getFirstname(), registerDTO.getLastname(), registerDTO.getBirthDate(), NodeVisibility.Private, tree, registerDTO.isMale(), user);
        nodeRepository.save(rootNode);
        //TODO: Send validation email

        userRepository.save(user);
        System.out.println("[USER] - Registering new user "+ registerDTO.getEmail());

        return ResponseEntity.ok(user);
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
