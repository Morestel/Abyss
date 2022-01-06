package com.pwa.project.abyss.models;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Data
@Entity
@Table(name = "PrivateMessage")
public class PrivateMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "sender_username", referencedColumnName = "username", nullable = false)
    private User sender;

    @OneToOne
    @JoinColumn(name = "receiver_username", referencedColumnName = "username", nullable = false)
    private User receiver;

    @Temporal(TemporalType.TIMESTAMP)
    private Date datePm;

    @Lob
    @Column(name = "message", columnDefinition = "BLOB NOT NULL")
    private String message;

    @Column(name = "read", columnDefinition = "boolean default false")
    private boolean read;

    public PrivateMessage() {
        /* */
    }

    public PrivateMessage(User sender, User receiver, Date datePm, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.datePm = datePm;
        this.message = message;
    }

    public boolean getRead(){
        return this.read;
    }
}