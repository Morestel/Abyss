package com.pwa.project.abyss.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
@Entity
public class Item {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    int id;
    String nameItem;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    Date day;
    String hourBeginItem;
    String hourEndItem;
    String roomItem;
    String descriptionItem;
    int idSchedule;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Item() {
    }

    public Item(String nameItem, Date day, String dateBeginItem, String dateEndItem, String placeItem,
            String descriptionItem) {
        this.nameItem = nameItem;
        this.day = day;
        this.hourBeginItem = dateBeginItem;
        this.hourEndItem = dateEndItem;
        this.roomItem = placeItem;
        this.descriptionItem = descriptionItem;
    }

    public Item(String nameItem, Date day, String dateBeginItem, String dateEndItem, String placeItem,
            Schedule schedule, String descriptionItem) {
        this.nameItem = nameItem;
        this.day = day;
        this.hourBeginItem = dateBeginItem;
        this.hourEndItem = dateEndItem;
        this.roomItem = placeItem;
        this.schedule = schedule;
        this.descriptionItem = descriptionItem;
    }

}