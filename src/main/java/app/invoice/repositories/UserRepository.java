package app.invoice.repositories;

import app.invoice.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;


public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional
    @Modifying
    void deleteUserByIdGreaterThanEqual(Long id);

    User getById(Long id);

    User getByUserName(String userName);

    @Modifying
    @Transactional
    User save(User user);

    @Modifying
    @Transactional
    void deleteById(Long id);
}
