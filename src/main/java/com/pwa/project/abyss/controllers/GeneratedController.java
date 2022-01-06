package com.pwa.project.abyss.controllers;

import java.util.Random;
import javax.inject.Inject;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GeneratedController {

    @Inject
    UserRepository aUserRepo;

    @RequestMapping("generate-user")
    public String GenerateUser() {
        /* Generate 50 random user */
        int nb_lettres = new Random().nextInt(10);
        for (int i = 0; i < 50; i++) {
            while (nb_lettres == 0) {
                nb_lettres = new Random().nextInt(10);
            }
            String mail = "";
            String password = "";
            String username = "";
            for (int j = 0; j < nb_lettres; j++) {
                mail += new Random().nextInt(10);
                password += new Random().nextInt(10);
                username += new Random().nextInt(10);

            }
            mail += "@gmail.com";
            aUserRepo.save(new User(username, mail, password));
        }
        return "redirect:/admin";
    }

    @RequestMapping("generate-items")
    public String GenerateItems() {

        return "redirect:/";
    }
}