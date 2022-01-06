package com.pwa.project.abyss.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwa.project.abyss.models.PrivateMessage;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.ItemRepository;
import com.pwa.project.abyss.repository.MessageRepository;
import com.pwa.project.abyss.repository.PrivateMessageRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
@EnableHypermediaSupport(type = HypermediaType.COLLECTION_JSON)
public class RestControllerUser {

    @Inject
    UserRepository aUserRepository;

    @Inject
    ScheduleRepository aScheduleRepository;

    @Inject
    ItemRepository aItemRepository;

    @Inject
    MessageRepository aMessageRepository;

    @Inject
    PrivateMessageRepository aPrivateMessageRepository;

    /**
     * Add a user in a schedule
     * @param schedule Schedule in which we add the user
     * @param username Username of the user
     */
    @PostMapping(value = "/api/users/{username}/schedule")
    public void addUserInSchedule(@RequestBody Schedule schedule, @PathVariable String username) {
        // We search the user then we retrieve his schedule and finally we add a new
        // schedule
        User vUser = aUserRepository.findUserByUsername(username);
        List<Schedule> listSchedule = vUser.getSchedule();
        listSchedule.add(schedule);
        vUser.setSchedule(listSchedule);
        // And we save the schedule too
        System.err.println(schedule.getId());
        Schedule sc = aScheduleRepository.findScheduleById(schedule.getId());
        List<User> listUser = sc.getUsers();
        sc.addUser(vUser);
        schedule.setUsers(listUser);
        schedule.setId(sc.getId());
        schedule.setItems(sc.getItems());
        schedule.setMessages(sc.getMessages());
        aScheduleRepository.save(schedule);
        aUserRepository.save(vUser);
    }

    /**
     * Load all the users
     * @param auth
     * @return All the users
     */
    @ResponseBody
    @GetMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadAllUsers(Authentication auth) {
        // We return the list of all the user of the website
        List<JSONObject> entities = new ArrayList<>();
        for (User vUser : aUserRepository.findAll()) {
            JSONObject entity = new JSONObject();
            entity.put("username", vUser.getUsername());
            entity.put("email", vUser.getEmail());
            entity.put("profile_picture", vUser.getProfile_picture());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Load the actual user
     * @param auth
     * @return the actual user
     */
    @ResponseBody
    @GetMapping(value = "/api/users/giveUser", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadUserInSchedule(Authentication auth) {

        JSONObject entity = new JSONObject();
        User vUser = aUserRepository.findUserByUsername(auth.getName());
        entity.put("username", vUser.getUsername());
        entity.put("email", vUser.getEmail());
        entity.put("profile_picture", vUser.getProfile_picture());
        entity.put("role", vUser.getRoles());

        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    /**
     * Load the information of one user
     * @param username username of the user
     * @return The information about the user
     */
    @ResponseBody
    @GetMapping(value = "/api/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadUserProfile(@PathVariable String username) {
        User vUser = aUserRepository.findUserByUsername(username);
        JSONObject entity = new JSONObject();
        entity.put("username", vUser.getUsername());
        entity.put("email", vUser.getEmail());
        entity.put("profile_picture", vUser.getProfile_picture());
        entity.put("role", vUser.getRoles());
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    /**
     * Delete an user
     * @param username username of the user we want to delete
     */
    @DeleteMapping(value = "/api/users/delete/{username}")
    public void deleteUser(@PathVariable String username) {
        User vUser = aUserRepository.findUserByUsername(username);
        for (PrivateMessage vPm: aPrivateMessageRepository.findAll()){ // We also delete all the private message
            if (vPm.getSender().getUsername().equals(username) || vPm.getReceiver().getUsername().equals(username)){
                aPrivateMessageRepository.delete(vPm);
            }
        }

        // We have to delete also the user in the schedule : If is the the owner we transfer the ownership to the next user or we delete the schedule
        List<Schedule> listSchedule = aScheduleRepository.findAllByOwner(username);
        vUser.setSchedule(new ArrayList<>());
        for (Schedule sc: listSchedule){
            if (sc.getUsers().size() > 1){ // He is not alone
                changeOwnership(sc);
                List<User> tempUser = new ArrayList<>();
                tempUser = sc.getUsers();
                tempUser.remove(vUser);
                sc.setUsers(tempUser);
                aScheduleRepository.save(sc);
            }
            else{
                sc.setUsers(new ArrayList<>());
                aScheduleRepository.save(sc);
                aScheduleRepository.delete(sc);
            }
        }
        
        aUserRepository.delete(vUser);
    }

    // Change the ownership of a schedule, suppose that there exist more than one user in the schedule
    public void changeOwnership(Schedule sc){
        sc.setOwner(sc.getUsers().get(0).getUsername());
        aScheduleRepository.save(sc);
    }
}