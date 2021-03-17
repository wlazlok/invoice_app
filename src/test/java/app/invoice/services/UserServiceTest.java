package app.invoice.services;

import app.invoice.BuildModels;
import app.invoice.models.User;
import app.invoice.repositories.UserRepository;
import app.invoice.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    public UserRepository userRepository;
    @MockBean
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    public UserService userService;


    @Disabled
    @Test
    void createUserTest() throws Exception {
        //given
        User user = new User();
        user.setId(1L);
        user.setUsername("test user to save");
        user.setPassword("password");
        user.setEmail("email@test.com");
        user.setNip("123123123");
        user.setStreet("Test Street");
        user.setPostalCode("12-234");
        user.setCity("Test City");
        user.setBankAccountNumber("123123123213");
        user.setConfirmPassword("password");
        Iterable<User> users = Arrays.asList();

        //when
        when(userRepository.findAll()).thenReturn(users);
        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("tionmsjmwoieu19023");

        //then
        User savedUser = userService.createUser(user);
        assertEquals(user, savedUser);
    }

    //change password

    @Test
    void findUserByUserNameTest() throws Exception {
        log.info("UserServiceTest.findUserByUserNameTest");
        //given
        User user = new User();
        user.setId(100L);
        user.setUsername("test user to save");
        user.setPassword("password");
        user.setEmail("email@test.com");
        user.setNip("123123123");
        user.setStreet("Test Street");
        user.setPostalCode("12-234");
        user.setCity("Test City");
        user.setBankAccountNumber("123123123213");
        user.setConfirmPassword("password");

        //when
        when(userRepository.getByUsername(anyString())).thenReturn(user);

        //then
        User userFound = userService.findUserByUserName(anyString());
        assertEquals(user, userFound);
    }

    @Test
    void getAllUsernamesTest() {
        log.info("UserServiceTest.getAllUsernamesTest");
        //given
        List<String> usernames = Arrays.asList("user", "user1", "user2");
        Iterable<User> users = BuildModels.mockUsers();

        //when
        when(userRepository.findAll()).thenReturn(users);

        //then
        List<String> foundUsers = userService.getAllUsernames();
        assertFalse(foundUsers.isEmpty());
        assertEquals(usernames, foundUsers);
    }

    @Test
    void createUserUsernameAlreadyExistTest() {
        log.info("UserServiceTest.createUserUsernameAlreadyExistTest");
        //given
        Iterable<User> users = BuildModels.mockUsers();
        User userToSave = BuildModels.mockUsers().get(0);

        //when
        when(userRepository.findAll()).thenReturn(users);

        //then
        try {
            userService.createUser(userToSave);
        } catch (Exception ex) {
            assertTrue(ex instanceof Exception);
            assertEquals("Username already exists!", ex.getMessage());
        }
    }

    @Test
    void findUserByUsernameFailTest() {
        log.info("UserServiceTest.findUserByUsernameFailTest");
        //given
        User user = BuildModels.mockUsers().get(0);

        //when
        when(userRepository.getByUsername(anyString())).thenReturn(null);

        //then
        User foundUser = null;
        try {
            foundUser = userService.findUserByUserName(user.getUsername());
        } catch (Exception ex) {
            assertTrue(ex instanceof Exception);
            assertNull(foundUser);
            assertEquals("User not found", ex.getMessage());
        }
    }

    @Test
    void saveUserTest() {
        log.info("UserServiceTest.saveUserTest");
        //given
        User user = BuildModels.mockUsers().get(0);

        //when
        when(userRepository.save(any())).thenReturn(user);

        //then
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals(user, savedUser);
    }

    @Test
    void getUserByIdTest() {
        log.info("UserServiceTest.getUserByIdTest");
        //when
        User user = BuildModels.mockUsers().get(0);

        //when
        when(userRepository.getById(anyLong())).thenReturn(user);

        //then
        User foundUser = userService.getUserById(user.getId());
        assertNotNull(foundUser);
        assertEquals(user, foundUser);
    }
}
