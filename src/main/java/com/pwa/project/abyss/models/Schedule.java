package com.pwa.project.abyss.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity

@Table(name = "schedule")
public class Schedule {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    int id;

    @Column(name = "nameSchedule")
    String nameSchedule;

    @Column(name = "owner")
    String owner;

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    List<Item> items = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name="user_id")
    List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "schedule", cascade = CascadeType.REMOVE)
    List<Message> messages = new ArrayList<>();

    public Schedule() {
        // Default constructor
    }

    public void addUser(User u){
        this.users.add(u);
    }

    public Schedule(String name, String owner) {
        super();
        this.nameSchedule = name;
        this.owner = owner;
    }

    public Schedule(String name, String owner, List<Item> items, List<User> user) {
        super();
        this.nameSchedule = name;
        this.owner = owner;
        this.users = user;
        this.items = items;
    }
}
