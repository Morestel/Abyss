package com.pwa.project.abyss.controllers;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import javax.inject.Inject;

import com.pwa.project.abyss.UploadForm;
import com.pwa.project.abyss.components.UserManagement;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ProfileUserController {
    @Inject
    UserRepository aUserRepo;

    @Inject
    UserManagement aManagement;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * This function is used for the access of the profile page
     * @param model
     * @param auth
     * @param username username of the profile page we want to access
     * @return The profile page
     */
    @RequestMapping("/user/profile/{username}")
    public String User_profile(Model model, Authentication auth, @PathVariable String username) {
        model.addAttribute("user", aUserRepo.findUserByUsername(username));
        User u = new User();
        u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);
        return ("user");
    }

    /***
     * Method to make change in the profile. Need to be the owner of this account to make modification.
     * Give an upload form to make possible the changement of profile picture
     * @param model
     * @param auth
     * @param username Username of the user we want to modify some information
     * @return The page of modification
     */
    @GetMapping(path = "/user/profile/{username}/update")
    public String User_change_profile(Model model, Authentication auth, @PathVariable String username) {
        model.addAttribute("u", new User());
        model.addAttribute("form", new UploadForm());

        User u = new User();

        if (!auth.getName().equals(username)) {
            // It's not your account
            return "redirect:/error/" + "NotYourAccount";
        } else {
            u = aUserRepo.findUserByUsername(auth.getName());
        }
        model.addAttribute("user_profile", u);
        return "update-user";
    }

    /**
     * This function is used for change de information of the profile of the user
     * @param model
     * @param username username of the actual user
     * @param auth
     * @param u user in which we have the modification to set to the actual user
     * @return The home page
     */
    @PostMapping(path = "user/profile/{username}/update")
    public String UpdateProfile(Model model, @PathVariable String username, Authentication auth, User u) {
        User vTemp;
        vTemp = aUserRepo.findUserByUsername(auth.getName());
        u.setProfile_picture(vTemp.getProfile_picture());
        u.setRoles(vTemp.getRoles());

        // Check if old password is the good one
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        if (!u.getTemporary_password().isEmpty()) {
            if (!encoder.matches(u.getTemporary_password(), vTemp.getEncodedPassword())) {
                return "redirect:/home";
            }
        }

        // If the user wants to update his email
        if (!u.getEmail().isEmpty()) {
            aManagement.setEmail(u, u.getEmail());
        } else {
            u.setEmail(vTemp.getEmail());
        }

        // If the user wants to update his password
        if (!u.getEncodedPassword().isEmpty()) {
            u.setEncodedPassword(passwordEncoder.encode(u.getEncodedPassword()));
            aManagement.setNewPassword(u, u.getEncodedPassword());
        }

        return "redirect:/home";
    }

    /**
     * This function is used for changing the profile picture<
     * @param model
     * @param uploadForm Contain the new image 
     * @param auth
     * @param username Username of the user which wants to make the modification of the avatar
     * @return
     */
    @PostMapping(path = "/user/{username}/upload")
    public String upload(Model model, UploadForm uploadForm, Authentication auth, @PathVariable String username) {

        if (uploadForm.getFile().isEmpty()) {
            return "update-user";
        }

        // Check if directory exist

        Path path = Paths.get("src/main/resources/static/images/avatar");
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (NoSuchFileException ex) {
                System.err.println(ex);
            } catch (IOException ex) {
                System.err.println(ex);
            }
        }

        int dot = uploadForm.getFile().getOriginalFilename().lastIndexOf(".");
        String extention = "";
        if (dot > 0) {
            extention = uploadForm.getFile().getOriginalFilename().substring(dot).toLowerCase();
        }

        if (!extention.equals(".png") && !extention.equals(".jpg") && !extention.equals(".gif")) {
            return "redirect:/home";
        }

        String filename = auth.getName();
        Path uploadfile = Paths.get("src/main/resources/static/images/avatar/" + filename + extention);

        try (OutputStream os = Files.newOutputStream(uploadfile, StandardOpenOption.CREATE)) {
            byte[] bytes = uploadForm.getFile().getBytes();
            os.write(bytes);
        } catch (IOException ex) {
            System.err.println(ex);
        }
        // Save the name of the picture in the database
        User u = aUserRepo.findUserByUsername(username);

        aManagement.setProfilePicture(u, filename + extention);

        return "redirect:/home";

    }
}