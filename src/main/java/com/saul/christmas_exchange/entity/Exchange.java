package com.saul.christmas_exchange.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "exchange")
public class Exchange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAssignment;

    @ManyToOne
    @JoinColumn(name = "giver_id", nullable = false)
    private Participant giver;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private Participant receiver;

    public Exchange() {
    }

    public Exchange(Integer idAssignment, Participant giver, Participant receiver) {
        this.idAssignment = idAssignment;
        this.giver = giver;
        this.receiver = receiver;
    }

    public Integer getIdAssignment() {
        return idAssignment;
    }

    public void setIdAssignment(Integer idAssignment) {
        this.idAssignment = idAssignment;
    }

    public Participant getGiver() {
        return giver;
    }

    public void setGiver(Participant giver) {
        this.giver = giver;
    }

    public Participant getReceiver() {
        return receiver;
    }

    public void setReceiver(Participant receiver) {
        this.receiver = receiver;
    }
}
