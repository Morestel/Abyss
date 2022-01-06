package com.pwa.project.abyss.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import lombok.Data;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @Column(name = "username")
    String username;

    @Column(name = "email")
    String email;

    @Column(name="encodedPassword")
    String encodedPassword;

    @Column(name = "profile_picture")
    String profile_picture;

    @Transient
    String temporary_password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "schedule_id", nullable = true)
    private List<Schedule> schedule = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private Set<UserRole> roles = new HashSet<>();

    public User() {
        /**/
    }

    public void addSchedule(Schedule schedule){
        this.schedule.add(schedule);
    }

    public User(String username, String email) {
        this();
        setUsername(username);
        setEmail(email);
        this.roles.add(UserRole.USER);
        setEncodedPassword(null);
        setProfile_picture("default.png");
    }

    public User(String username, String email, String password) {
        this();
        setUsername(username);
        setEmail(email);
        setEncodedPassword(password);
        this.roles.add(UserRole.USER);
        setProfile_picture("default.png");
    }

    public User(String username, String email, String encodedPassword, List<String> roles) {
        this();
        setUsername(username);
        setEmail(email);
        setEncodedPassword(encodedPassword);
        this.roles.addAll(roles.stream().map(UserRole::valueOf).collect(Collectors.toList()));
        this.roles.add(UserRole.USER);
        setProfile_picture("default.png");
    }

    public boolean isOwner(Schedule schedule) {

        if (schedule.getOwner().equals(this.getUsername())) {
            return true;
        }

        else {
            return false;
        }
    }
}
