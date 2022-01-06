package com.pwa.project.abyss.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pwa.project.abyss.components.UserManagement;
import com.pwa.project.abyss.models.Item;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.models.UserRole;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Inject
    UserRepository aUserRepo;

    @Inject
    UserManagement aManagement;

    @Inject
    ScheduleRepository aScheduleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;
   
    /**
     * This function give the list of all the schedule which concerns the user then redirect to the home page.
     * @param model
     * @param auth
     * @return The home page
     */
    @RequestMapping({ "home", "" })
    public String home(Model model, Authentication auth) {
        /* Add the actual user of the session */
        User u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);

        /* Add the schedule of this user */
        List<Schedule> listSchedule = new ArrayList<>();
        List<Schedule> all_schedule = aScheduleRepo.findAllByOrderByNameSchedule();
        // We search in all the schedule
        for (Schedule s : all_schedule) {
            // We check all the user of all the schedule
            for (User user : s.getUsers()) {
                // If it's our session we put this in another list that we will send to the html
                if (user.getUsername().equals(auth.getName())) {
                    listSchedule.add(s);
                }
            }
        }
        // Finaly we send the final list
        model.addAttribute("listSchedule", listSchedule);
        return "home";
    }

    /**
     * This function redirect to the inscription page
     * @param model
     * @return The page of inscription
     */
    @RequestMapping("inscription")
    public String inscriptions(Model model) {
        model.addAttribute("u", new User());
        return "inscription";
    }

    /**
     * This function redirect to the login page
     * @return The login page
     */
    @RequestMapping("login")
    public String accessLogin() {
        return "login";
    }

    /**
     * Function which redirect to the home page after the login
     * @param model
     * @return The home page
     */
    @PostMapping(value = "/login")
    public String loginPage(Model model) {
        return "/home";
    }

    /**
     * This function allow the user to logout.
     * @param request
     * @param response
     * @param model
     * @param auth
     * @return Redirect to the login page
     */
    @GetMapping(value = "/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model, Authentication auth) {
        new SecurityContextLogoutHandler().logout(request, response, auth);
        return "redirect:/login?logout=true";
    }

    /**
     * Function which comes after the inscription, we add the user in the database after the encryption of his password.
     * @param u User to add in the database
     * @return Return the login page
     */
    @PostMapping(path = "/user/new")
    public String addUser(User u) {
        User vUser = new User(u.getUsername(), u.getEmail());
        vUser.setEncodedPassword(passwordEncoder.encode(u.getEncodedPassword()));
        aUserRepo.save(vUser);
        return "redirect:/login";
    }

    /**
     * Function to access to the admin page, require to be admin of the website
     * @param model
     * @param auth
     * @return The page admin if admin, the page error if not
     */
    @RequestMapping("/admin")
    public String Admin(Model model, Authentication auth) {

        User u = new User();
        u = aUserRepo.findUserByUsername(auth.getName());

        boolean isAdmin = false;

        for (UserRole s : u.getRoles()) {
            if (s.name().equals("ADMIN")) {
                isAdmin = true;
            }
        }
        if (!isAdmin) {
            model.addAttribute("isAdmin", false);
        }
        else{
            model.addAttribute("isAdmin", true);
        }

        model.addAttribute("user_profile", u);
        model.addAttribute("userList", aUserRepo.findAll());
        return "administration";
    }

    /**
     * This function delete an user based on the username which is in the URL
     * @param model
     * @param auth
     * @param username Username of the user we want to delete
     * @return The login page
     */
    @RequestMapping("/user/{username}/delete")
    public String Admin_delete_user(Model model, Authentication auth, @PathVariable String username) {
        User u = aUserRepo.findUserByUsername(username);
        System.err.println(u.getUsername());
        aUserRepo.delete(u);
        model.addAttribute("user_profile", aUserRepo.findUserByUsername(auth.getName()));
        return "redirect:/admin";

    }

    /**
     * Make an user admin
     * @param username Username of the user we want to make admin
     * @return The home page
     */
    @RequestMapping("/user/{username}/makeAdmin")
    public String makeAdmin(@PathVariable String username) {

        aManagement.makeUserAdmin(username);
        return "redirect:/home";
    }

    /**
     * This function redirect to the actuality page, in which we have the list of the item of the day of the actual user.
     * @param model
     * @param auth
     * @return The page of actuality
     */
    @RequestMapping({ "actualite" })
    public String actualite(Model model, Authentication auth) {
        User u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);
        List<Item> itemsList = new ArrayList<>();
        List<Schedule> all_schedule = aScheduleRepo.findAllByOrderByNameSchedule();
        List<Schedule> listSchedule = new ArrayList<>();

        // We search in all the schedule
        for (Schedule s : all_schedule) {
            // We check all the user of all the schedule
            for (User user : s.getUsers()) {
                // If it's our session we put this in another list that we will send to the html
                if (user.getUsername().equals(auth.getName())) {
                    listSchedule.add(s);
                }
            }
        }
        for (Schedule sc : listSchedule) {
            itemsList.addAll(sc.getItems());
        }
        // Date of the day
        Date date = new Date();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");
        List<Item> sortedListItem = new ArrayList<>();

        for (Item i : itemsList) {

            if (dateOnly.format(i.getDay()).equals(dateOnly.format(date))) {
                sortedListItem.add(i);
            }
        }

        model.addAttribute("listItem", sortedListItem);

        return "actualite";
    }

    /**
     * This function give an access to the help page
     * @param model
     * @param auth
     * @return The help page
     */
    @RequestMapping({ "aide" })
    public String help(Model model, Authentication auth) {
        User u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);
        return "help";
    }

    /* Function in the project order */
    public String escape(String v) throws UnsupportedEncodingException {
        return URLEncoder.encode(v, "UTF-8").replace("+", "%20");
    }

    /**
     * This function allow to contact the admin of the website
     * @param pSubject Subject of the mail
     * @param pBody Body of the mail
     * @return 
     * @throws UnsupportedEncodingException
     */
    @PostMapping(path = "/contactUs")
    public String contactUs(@RequestParam(defaultValue = "") String pSubject,
            @RequestParam(defaultValue = "") String pBody) throws UnsupportedEncodingException {

        String subject = "[HELP] " + pSubject;
        String email = "email@gmail.com";

        return "redirect:mailto:" + email + "?subject=" + escape(subject) + "&body=" + escape(pBody);
    }

    /**
     * This function is used to access to the error page
     * @param model
     * @param auth
     * @param reason PathVariable which give the reason why the error page is displaying
     * @return The error page
     */
    @RequestMapping("error/{reason}")
    public String error(Model model, Authentication auth, @PathVariable String reason) {
        User u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);
        model.addAttribute("error", reason);

        return "error";
    }
}