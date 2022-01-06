package com.pwa.project.abyss.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.pwa.project.abyss.components.UserManagement;
import com.pwa.project.abyss.models.PrivateMessage;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.PrivateMessageRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PrivateMessageController {

    @Inject
    UserRepository aUserRepository;

    @Inject
    UserManagement aManagement;

    @Inject
    ScheduleRepository aScheduleRepository;

    @Inject
    PrivateMessageRepository aPrivateMessageRepository;

    /**
     * This function give an access to the mailbox. 
     * We give to the model the list of one private message for each user whatever if we are the sender or the receiver of the message.
     * All the other users will be display in the mailbox with the last message in the conversation.
     * The list is sort so that the last message will be the first to be display
     * @param model
     * @param auth
     * @return
     */
    @RequestMapping("/pm")
    public String mailBox(Model model, Authentication auth) {
        User u = aUserRepository.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);

        List<PrivateMessage> listPrivateMessage = new ArrayList<>();
        List<PrivateMessage> listMessage = new ArrayList<>();

        for (User vUser : aUserRepository.findAll()) {
            listPrivateMessage = aPrivateMessageRepository.findAllOrderByDate(u.getUsername(), vUser.getUsername());
            if (listPrivateMessage.size() > 0) {
                listMessage.add(listPrivateMessage.get(0)); // We take only the last message to display it
            }
        }
        model.addAttribute("listPrivateMessage", listMessage);
        return "mailbox";
    }

    /**
     * This function guides to the private message page between the actual user and the user whose the name is specified in the url
     * @param model
     * @param auth
     * @param username username of the other user
     * @return The page of the private message
     */
    @RequestMapping("/pm/{username}")
    public String privateMessage(Model model, Authentication auth, @PathVariable(required = true) String username){
        User actualUser = aUserRepository.findUserByUsername(auth.getName());
        
        List<PrivateMessage> listPrivateMessage = aPrivateMessageRepository.findAllOrderByDate(auth.getName(), username);

        // In the model we add the list of the private message, the actual user and the other user (To have access to profile picture for example)
        model.addAttribute("otherUser", aUserRepository.findUserByUsername(username));
        model.addAttribute("user_profile", actualUser);
        model.addAttribute("listPrivateMessage", listPrivateMessage);

        return "privateMessagePage";
    }

    /**
     * This function allows to add a message in the private message between our user and another one
     * @param model
     * @param auth
     * @param username The user that we want to send a pm
     * @return Return the page of discussion between the two users 
     */
    @PostMapping(value = "/pm/{username}/addMessage")
    public String addPrivateMessage(Model model, Authentication auth, @PathVariable(required = true) String username, @RequestParam String newMessage){
        PrivateMessage vPm = new PrivateMessage(aUserRepository.findUserByUsername(auth.getName()), aUserRepository.findUserByUsername(username), new Date(), newMessage);
        aPrivateMessageRepository.save(vPm);
        
        return "redirect:/pm/" + username;
    }
}