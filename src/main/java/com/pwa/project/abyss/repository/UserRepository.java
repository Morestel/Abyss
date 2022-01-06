package com.pwa.project.abyss.repository;

import java.util.List;
import com.pwa.project.abyss.models.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {

    User findUserByUsername(String username);

    int findIdByEmail(String email);

    User findUserByEmail(String email);

    User findByEmail(String email);

    List<User> findAllByOrderByUsername();

}
