package com.pwa.project.abyss.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Message {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    int id;

    @Column(name = "message")
    String message;

    @Column(name = "sender")
    String sender;

    int idSchedule;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public Message() {

    }

    public Message(String sender, String message) {
        setSender(sender);
        setMessage(message);
    }

    public Message(String sender, String message, Schedule schedule) {
        setSender(sender);
        setMessage(message);
        setSchedule(schedule);
    }
}