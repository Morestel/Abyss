package com.pwa.project.abyss.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.pwa.project.abyss.models.Item;
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
public class RestControllerItem {

    @Inject
    UserRepository aUserRepository;

    @Inject
    ScheduleRepository aScheduleRepository;

    @Inject
    ItemRepository aItemRepository;

    @Inject
    MessageRepository aMessageRepository;

    /**
     * Load the item in a schedule which is given in the path
     * @param idSchedule ID of the schedule
     * @return The list of the item
     */
    @ResponseBody
    @GetMapping(value = "/api/schedules/{idSchedule}/items", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadItem(@PathVariable String idSchedule) {
        // We search the schedule
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        List<JSONObject> entities = new ArrayList<>();
        for (Item vItem : sc.getItems()) {
            JSONObject entity = new JSONObject();
            entity.put("id", vItem.getId());
            entity.put("nameItem", vItem.getNameItem());
            entity.put("day", vItem.getDay());
            entity.put("descriptionItem", vItem.getDescriptionItem());
            entity.put("roomItem", vItem.getRoomItem());
            entity.put("hourBeginItem", vItem.getHourBeginItem());
            entity.put("hourEndItem", vItem.getHourEndItem());
            entities.add(entity);
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Load the item for the actuality page
     * @param username Username of the user's actuality
     * @return The items
     */
    @ResponseBody
    @GetMapping(value = "/api/items/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> loadItemForActu(@PathVariable String username) {
        // We search the schedule
        User vUser = aUserRepository.findUserByUsername(username);
        List<JSONObject> entities = new ArrayList<>();
        Date date = new Date();
        SimpleDateFormat dateOnly = new SimpleDateFormat("yyyy-MM-dd");

        for (Schedule sc : vUser.getSchedule()) {
            for (Item vItem : sc.getItems()) {
                if (dateOnly.format(vItem.getDay()).equals(dateOnly.format(date))) {
                    JSONObject entity = new JSONObject();
                    entity.put("id", vItem.getId());
                    entity.put("nameItem", vItem.getNameItem());
                    entity.put("day", vItem.getDay());
                    entity.put("descriptionItem", vItem.getDescriptionItem());
                    entity.put("roomItem", vItem.getRoomItem());
                    entity.put("hourBeginItem", vItem.getHourBeginItem());
                    entity.put("hourEndItem", vItem.getHourEndItem());
                    entities.add(entity);
                }
            }
        }

        return new ResponseEntity<>(entities, HttpStatus.OK);
    }

    /**
     * Creation of an item
     * @param item Item which has to be created
     * @param idSchedule Schedule in which we will put the item
     */
    @PostMapping(value = "/api/items/{idSchedule}")
    public void createItem(@RequestBody Item item, @PathVariable String idSchedule) {
        // We search the schedule
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        item.setSchedule(sc);
        List<Item> listeItemSchedule = sc.getItems();
        listeItemSchedule.add(item);
        // Now we save the item and the schedule
        aItemRepository.save(item);
        aScheduleRepository.save(sc);
    }

    /**
     * Update an item from a schedule
     * @param item Information on the updated item 
     * @param idSchedule Schedule in which we will update the item
     */
    @PostMapping(value = "/api/items/{idSchedule}/modify")
    public void updateItem(@RequestBody Item item, @PathVariable String idSchedule) {
        // We search the schedule
        Schedule sc = aScheduleRepository.findScheduleById(Integer.parseInt(idSchedule));
        item.setSchedule(sc);
        List<Item> listeItemSchedule = sc.getItems();
        listeItemSchedule.add(item);
        // Now we save the item and the schedule
        aItemRepository.save(item);
        aScheduleRepository.save(sc);
    }
}