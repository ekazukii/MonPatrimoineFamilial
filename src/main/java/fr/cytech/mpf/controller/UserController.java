package fr.cytech.mpf.controller;

import fr.cytech.mpf.config.MustBeLogged;
import fr.cytech.mpf.dto.LoginDTO;
import fr.cytech.mpf.dto.RegisterDTO;
import fr.cytech.mpf.dto.UserAddDTO;
import fr.cytech.mpf.dto.UserGetDTO;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.UserRepository;
import fr.cytech.mpf.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

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
    public ResponseEntity<User> testUser(@RequestBody LoginDTO loginDTO, HttpSession session) {
        Optional<User> user = userRepository.findUserByUsernameAndPassword(loginDTO.getUsername(), loginDTO.getPassword());
        if(user.isEmpty()) return ResponseEntity.notFound().build();
        session.setAttribute("account", user.get());
        return ResponseEntity.ok(user.get());
    }

    @CrossOrigin
    @PostMapping("/register")
    public ResponseEntity<User> registerNewUser(@RequestBody RegisterDTO registerDTO, HttpSession session) {
        User user = modelMapper.map(registerDTO, User.class);
        userRepository.save(user);
        session.setAttribute("account", user);
        return ResponseEntity.ok(user);
    }

    @CrossOrigin
    @GetMapping("/userinfo")
    public ResponseEntity<User> getUserInfo(HttpSession session) {
        User usr = (User) session.getAttribute("account");
        System.out.println(usr);
        return ResponseEntity.ok(usr);
    }

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
    @GetMapping(value = "/user")
    public ResponseEntity<UserGetDTO> getUser(@RequestParam Long id) {
        User user = userRepository.getReferenceById(id);
        UserGetDTO userGetDTO = modelMapper.map(user, UserGetDTO.class);
        return ResponseEntity.ok(userGetDTO);
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
