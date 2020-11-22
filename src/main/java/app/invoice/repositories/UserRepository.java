package app.invoice.repositories;

import app.invoice.models.GoodsAndServices;
import app.invoice.models.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;

public interface UserRepository extends CrudRepository<User, Long> {

    @Transactional
    @Modifying
    void deleteUserByIdGreaterThanEqual(Long id);

    User getById(Long id);

    User getByUserName(String userName);
}
