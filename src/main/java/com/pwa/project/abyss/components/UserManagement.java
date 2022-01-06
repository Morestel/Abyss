package com.pwa.project.abyss.components;

import javax.inject.Inject;

import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.models.UserRole;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserManagement implements UserDetailsService {

    @Inject
    UserRepository repo;

    public final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userOrEmpty = repo.findById(username);
        if (userOrEmpty.isEmpty()) {
            throw new UsernameNotFoundException(username);
        }
        User user = userOrEmpty.get();
        return new org.springframework.security.core.userdetails.User(username, user.getEncodedPassword(),
                user.getRoles());
    }

    public void setProfilePicture(User u, String path_picture) {
        u.setProfile_picture(path_picture);
        repo.save(u);
    }

    public void setEmail(User u, String email) {
        u.setEmail(email);
        repo.save(u);
    }

    public void setNewPassword(User u, String password) {
        u.setEncodedPassword(password);
        repo.save(u);
    }

    public void makeUserAdmin(String username) {
        User u = repo.findById(username).orElse(null);
        u.getRoles().add(UserRole.ADMIN);
        repo.save(u);
    }
}