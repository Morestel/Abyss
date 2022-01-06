package com.pwa.project.abyss.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwa.project.abyss.models.Message;
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
public class RestControllerMessage {

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
     * Load the private messages between the actual user and the other user
     * @param actualUsername Username of the actual user
     * @param otherUsername Username of the other user
     * @return The private message
     */
    @ResponseBody
    @GetMapping(value = "/api/privateMessage/{actualUsername}/{otherUsername}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadPrivateMessages(@PathVariable String actualUsername,
            @PathVariable String otherUsername) {
        List<PrivateMessage> listPrivateMessage = aPrivateMessageRepository.findAllOrderByDate(actualUsername,
                otherUsername);
        List<JSONObject> entities = new ArrayList<>();
        String pattern = "YYYY-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        for (PrivateMessage vPm : listPrivateMessage) {
            JSONObject entity = new JSONObject();
            entity.put("date", df.format(vPm.getDatePm()));
            entity.put("receiver", vPm.getReceiver().getUsername());
            entity.put("sender", vPm.getSender().getUsername());
            entity.put("message", vPm.getMessage());
            entities.add(entity);
        }
        
        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Load the number of notification for the actual user
     * @param actualUsername Name of the actual user
     * @return The number of notification
     */
    @ResponseBody
    @GetMapping(value = "/api/privateMessage/notif/{actualUsername}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadNumberNotif(@PathVariable String actualUsername) {
        List<PrivateMessage> listPrivateMessage = aPrivateMessageRepository.findAllByReceiver(actualUsername);
        JSONObject entity = new JSONObject();
        int numberOfMessages = 0;

        // Browsing of the list
        for (PrivateMessage vPm: listPrivateMessage){
            if (!vPm.getRead()){ // Unread message
                numberOfMessages++;
            }
        }
        entity.put("number", numberOfMessages);
        return new ResponseEntity<>(entity, HttpStatus.OK);
    }

    /**
     * Load a conversation between two users.
     * Sort the private message by date
     * @param actualUsername Username of the actual user
     * @return The list sorted of the private message
     */
    @ResponseBody
    @GetMapping(value = "/api/privateMessage/getConversation/{actualUsername}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadConversation(@PathVariable String actualUsername) {
        List<PrivateMessage> listPrivateMessage = new ArrayList<>();
        List<JSONObject> entities = new ArrayList<>();
        String pattern = "YYYY-MM-dd HH:mm:ss";
        SimpleDateFormat df = new SimpleDateFormat(pattern);

        for (User vUser : aUserRepository.findAll()) {
            listPrivateMessage = aPrivateMessageRepository.findAllOrderByDate(actualUsername, vUser.getUsername());
            if (listPrivateMessage.size() > 0) {
                PrivateMessage vPm = listPrivateMessage.get(0); // We take only the last message to display it
                JSONObject entity = new JSONObject();
                entity.put("date", df.format(vPm.getDatePm()));
                entity.put("receiver", vPm.getReceiver().getUsername());
                entity.put("receiver_picture", vPm.getReceiver().getProfile_picture());
                entity.put("sender", vPm.getSender().getUsername());
                entity.put("sender_picture", vPm.getSender().getProfile_picture());
                entity.put("message", vPm.getMessage());
                entities.add(entity);
            }
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Lecture of a private message, make at true the reading of the private message,
     * so the notification didn't appear anymore
     * @param actualUsername Username of the actual user
     * @param otherUsername Username of the other user
     */
    @PostMapping(value = "/api/privateMessage/read/{actualUsername}/{otherUsername}")
    public void readPrivateMessage(@PathVariable String actualUsername, @PathVariable String otherUsername) {
        // Set to true all the private message when the other is the sender and our user is the receiver
        List<PrivateMessage> listPm = aPrivateMessageRepository.findAllByReceiverAndSender(actualUsername, otherUsername);
        for (PrivateMessage vPm : listPm){
            vPm.setRead(true);
            aPrivateMessageRepository.save(vPm);
        }
    }

    /**
     * Add a private message
     * @param pm Private message to add
     */
    @PostMapping(value = "/api/privateMessage/addMessage")
    public void addPrivateMessage(@RequestBody PrivateMessage pm) {
        aPrivateMessageRepository.save(pm);
    }

    /**
     * Add a message in a schedule
     * @param message Message to add
     * @param idSchedule Schedule in which we add the message
     */
    @PostMapping(value = "/api/messages/add/{idSchedule}")
    public void addMessage(@RequestBody Message message, @PathVariable String idSchedule) {
        Schedule schedule = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        message.setSchedule(schedule);
        aMessageRepository.save(message);
    }

    /**
     * Load all the message in a schedule
     * @param idSchedule The schedule in which we want to load the message
     * @return The list of all the Message in the schedule
     */
    @ResponseBody
    @GetMapping(value = "/api/schedules/{idSchedule}/messages/", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadMessages(@PathVariable String idSchedule) {
        List<Message> listMessage = aMessageRepository.findAllOrderById(Integer.parseInt(idSchedule));
        List<JSONObject> entities = new ArrayList<>();
        for (Message m : listMessage) {
            JSONObject entity = new JSONObject();
            entity.put("message", m.getMessage());
            entity.put("sender", m.getSender());
            entity.put("id", m.getId());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Delete a message in the schedule
     * @param idMessage Id of the message which has to be delete
     */
    @DeleteMapping(value = "/api/messages/delete/{idMessage}")
    public void deleteMessage(@PathVariable String idMessage) {
        Message m = aMessageRepository.findById(Integer.parseInt(idMessage));
        aMessageRepository.delete(m);
    }
}