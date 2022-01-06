package com.pwa.project.abyss.controllers;

import java.util.List;

import javax.inject.Inject;

import com.pwa.project.abyss.models.Message;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.MessageRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MessageController {
    @Inject
    ScheduleRepository scheduleRepo;

    @Inject
    MessageRepository messageRepo;

    @Inject
    UserRepository aUserRepo;

    /**
     * This function is used for add a message of a specific schedule in the database
     * @param message The message that we want to write and add in the database
     * @param auth
     * @param id ID of the schedule in which we want to add a message
     * @param model
     * @return The page of the actual schedule
     */
    @PostMapping(path = "/schedule/{id}/addMessages")
    public String addMessage(@RequestParam String message, Authentication auth, @PathVariable int id, Model model) {
        User u = new User();
        u = aUserRepo.findUserByUsername(auth.getName());
        model.addAttribute("user_profile", u);

        String sender = auth.getName();

        Schedule schedule = scheduleRepo.findScheduleById(id);
        List<Message> listeMessage = schedule.getMessages();

        Message newMessage = new Message(sender, message, schedule);
        messageRepo.save(newMessage);

        listeMessage.add(newMessage);
        schedule.setMessages(listeMessage);
        scheduleRepo.save(schedule);

        return "redirect:/schedule/" + id + "#posTableMessage";
    }
}