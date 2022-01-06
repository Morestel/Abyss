package com.pwa.project.abyss.controllers;

import javax.inject.Inject;

import com.pwa.project.abyss.models.Item;
import com.pwa.project.abyss.repository.ItemRepository;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    @Inject
    ItemRepository itemRepo;

    /**
     * Modify an item in the database
     * @param nameOld
     * @param nameItem
     * @param dateBeginItem
     * @param dateEndItem
     * @param roomItem
     * @return 
     */
    @PostMapping(value = "/modifyItem")
    public String modifyItem(@RequestParam String nameOld, @RequestParam String nameItem,
            @RequestParam String dateBeginItem, @RequestParam String dateEndItem, @RequestParam String roomItem) {
        if (itemRepo.findAllByOrderByNameItem().contains(itemRepo.findByNameItem(nameOld))) {
            Item item = itemRepo.findByNameItem(nameOld);
            item.setNameItem(nameItem);
            item.setHourBeginItem(dateBeginItem);
            item.setHourEndItem(dateEndItem);
            item.setRoomItem(roomItem);
            itemRepo.save(item);
        }
        return "/home";
    }
}
