package fr.cytech.mpf.controller;

import fr.cytech.mpf.dto.UserAddDTO;
import fr.cytech.mpf.dto.UserGetDTO;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.UserRepository;
import fr.cytech.mpf.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.EntityResponse;

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
    @PostMapping(value = "/addtestuser")
    public ResponseEntity<String> addTestUser() {
        User user = new User();
        user.setEmail("baptisteloison8400@gmail.com");
        user.setFirstname("baptiste");
        user.setLastname("baptiste");
        user.setPassword("trqllemdpestpashashosef");
        user.setUsername("xX_D4rK_B4pt1sT3_Xx");
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
