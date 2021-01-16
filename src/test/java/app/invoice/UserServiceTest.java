package app.invoice;

import app.invoice.models.User;
import app.invoice.repositories.UserRepository;
import app.invoice.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    public UserRepository userRepository;
    @MockBean
    public BCryptPasswordEncoder bCryptPasswordEncoder;
    @InjectMocks
    public UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.initMocks(this);
    }

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
        //when(userRepository.save(any(User.class))).thenReturn(user);

        //then
        User savedUser = userService.createUser(user);
        assertEquals(user, savedUser);
    }

    //change password

    @Test
    void findUserByUserNameTest() throws Exception {
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
}
