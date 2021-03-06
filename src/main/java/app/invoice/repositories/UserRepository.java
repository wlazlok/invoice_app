package app.invoice.repositories;

import app.invoice.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

@Transactional
public interface UserRepository extends CrudRepository<User, Long> {

    void deleteUserByIdGreaterThanEqual(Long id);

    User getById(Long id);

    User getByUsername(String userName);

    User save(User user);

    void deleteById(Long id);
}
