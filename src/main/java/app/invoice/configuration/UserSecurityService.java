package app.invoice.configuration;

import app.invoice.models.User;
import app.invoice.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserSecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User userFound = userRepository.getByUsername(s);
        if (userFound == null) {
            throw new UsernameNotFoundException("Błędny login lub hasło");
        }
        return userFound;
    }
}
