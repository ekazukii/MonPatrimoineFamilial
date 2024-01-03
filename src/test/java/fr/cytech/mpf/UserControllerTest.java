package fr.cytech.mpf;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.cytech.mpf.controller.UserController;
import fr.cytech.mpf.dto.LoginDTO;
import fr.cytech.mpf.dto.UserAddDTO;
import fr.cytech.mpf.entity.User;
import fr.cytech.mpf.repository.NodeRepository;
import fr.cytech.mpf.repository.TreeRepository;
import fr.cytech.mpf.repository.UserRepository;
import fr.cytech.mpf.service.MailService;
import fr.cytech.mpf.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    UserService userService;
    @MockBean
    MailService mailService;
    @MockBean
    TreeRepository treeRepository;
    @MockBean
    NodeRepository nodeRepository;


    @Test
    public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception {
        User user = new User(10, "lastname", "firstname", "email", "password", "username", true, null, null, true, null);
        User user2 = new User(20, "lastname2", "firstname2", "email2", "password2", "username2", true, null, null, true, null);
        List<User> allUsers = Arrays.asList(user, user2);

        given(userRepository.findAll()).willReturn(allUsers);

        mvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(user.getUsername())))
                .andExpect(jsonPath("$[1].username", is(user2.getUsername())));
    }


    @Test
    public void whenLoginWithValidCredentials_thenReturnsUserAndSetsSession() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("testUser", "password");
        User user = new User(); // Create a user object with appropriate fields set
        String hashedPassword = "hashedPassword";
        MockHttpSession session = new MockHttpSession();

        given(userService.passwordToHash(loginDTO.getPassword())).willReturn(hashedPassword);
        given(userRepository.findUserByUsernameAndPassword(loginDTO.getUsername(), hashedPassword))
                .willReturn(Optional.of(user));

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDTO);

        // Act and Assert
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDtoJson)
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername()))); // Modify as per your User class

        // Optional: Verify session attribute
        assertEquals(user, session.getAttribute("account"));
    }


    @Test
    public void whenLoginWithInvalidPassword_thenReturnsNotFound() throws Exception {
        // Arrange
        LoginDTO loginDTO = new LoginDTO("testUser", "wrongPassword");
        String hashedPassword = "hashedWrongPassword";
        MockHttpSession session = new MockHttpSession();

        given(userService.passwordToHash(loginDTO.getPassword())).willReturn(hashedPassword);
        given(userRepository.findUserByUsernameAndPassword(loginDTO.getUsername(), hashedPassword))
                .willReturn(Optional.empty()); // User not found because of wrong password

        ObjectMapper objectMapper = new ObjectMapper();
        String loginDtoJson = objectMapper.writeValueAsString(loginDTO);

        // Act and Assert
        mvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginDtoJson)
                        .session(session))
                .andExpect(status().isNotFound()); // Expecting 404 Not Found response

        // Optional: Verify session attribute is not set
        assertNull(session.getAttribute("account"));
    }
}