package com.pwa.project.abyss.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwa.project.abyss.models.Schedule;
import com.pwa.project.abyss.models.User;
import com.pwa.project.abyss.repository.ItemRepository;
import com.pwa.project.abyss.repository.MessageRepository;
import com.pwa.project.abyss.repository.ScheduleRepository;
import com.pwa.project.abyss.repository.UserRepository;

import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.minidev.json.JSONObject;

@RestController
@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
@EnableHypermediaSupport(type = HypermediaType.COLLECTION_JSON)
public class RestControllerSchedule {

    @Inject
    UserRepository aUserRepository;

    @Inject
    ScheduleRepository aScheduleRepository;

    @Inject
    ItemRepository aItemRepository;

    @Inject
    MessageRepository aMessageRepository;

    /**
     * Create a schedule in VueJS
     * @param schedule The schedule that had to be created
     */
    @PostMapping(value = "/api/schedules")
    public void createSchedule(@RequestBody Schedule schedule) {
        // We search the user then we retrieve his schedule and finally we add a new
        // schedule
        User vUser = aUserRepository.findUserByUsername(schedule.getOwner());
        List<Schedule> listSchedule = vUser.getSchedule();
        listSchedule.add(schedule);
        vUser.setSchedule(listSchedule);
        // And we save the schedule too
        List<User> lUser = schedule.getUsers();
        lUser.add(vUser);
        schedule.setUsers(lUser);   

        aScheduleRepository.save(schedule);
        aUserRepository.save(vUser);
    }

    /**
     * This function delete a schedule
     * @param idSchedule ID of the schedule that had to be deleted
     */
    @DeleteMapping(value = "/api/schedule/delete/{idSchedule}")
    public void deleteSchedule(@PathVariable String idSchedule){
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        sc.setUsers(new ArrayList<>());
        aScheduleRepository.save(sc);
        // Find and delete the schedule for all the users
        for (User vUser: aUserRepository.findAllByOrderByUsername()){
            if (vUser.getSchedule().contains(sc)){
                List<Schedule> listSchedule = vUser.getSchedule();
                listSchedule.remove(sc);
                vUser.setSchedule(listSchedule);
                aUserRepository.save(vUser);
            }
        }

        aScheduleRepository.delete(sc);
    }

    /**
     * Load all the user which participate in the schedule
     * @param idSchedule ID of the schedule
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/api/schedules/{idSchedule}/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadUserInSchedule(@PathVariable String idSchedule) {
        // We search the schedule
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        List<JSONObject> entities = new ArrayList<>();
        for (User vUser : sc.getUsers()) {
            JSONObject entity = new JSONObject();
            entity.put("username", vUser.getUsername());
            entity.put("email", vUser.getEmail());
            entity.put("profile_picture", vUser.getProfile_picture());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Load the actual schedule
     * @param idSchedule ID of the schedule that we want to load
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/api/schedule/{idSchedule}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadOneSchedule(@PathVariable String idSchedule){
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        JSONObject entity = new JSONObject();
        entity.put("id", sc.getId());
        entity.put("nameSchedule", sc.getNameSchedule());
        entity.put("owner", sc.getOwner());
        return new ResponseEntity<>(entity, HttpStatus.OK);

    }
    
    /**
     * Load all the schedule of an user
     * @param username User that we want to load the schedule
     * @return
     */
    @ResponseBody
    @GetMapping(value = "/api/users/{username}/schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadSchedule(@PathVariable String username) {
        User vUser = aUserRepository.findUserByUsername(username);

        List<JSONObject> entities = new ArrayList<JSONObject>();
        for (Schedule s : vUser.getSchedule()) {
            JSONObject entity = new JSONObject();
            entity.put("id", s.getId());
            entity.put("nameSchedule", s.getNameSchedule());
            entity.put("owner", s.getOwner());
            entities.add(entity);
        }
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    @PutMapping("/api/schedule/{idSchedule}/delete/{username}")
    public void kickUser(@PathVariable int idSchedule, @PathVariable String username){
        User vUser = aUserRepository.findUserByUsername(username);
        Schedule vSchedule = aScheduleRepository.findScheduleById(idSchedule);
        
        List<Schedule> listSchedule = vUser.getSchedule();
        List<User> listUser = vSchedule.getUsers();
                                        
        if (listSchedule.contains(vSchedule) && listUser.contains(vUser)){
            listSchedule.remove(vSchedule);
            listUser.remove(vUser);

            vUser.setSchedule(listSchedule);
            vSchedule.setUsers(listUser);

            aUserRepository.save(vUser);
            aScheduleRepository.save(vSchedule);
        }
    }
}