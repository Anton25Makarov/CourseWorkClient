package by.bsuir.entities;

import java.util.Date;
import java.util.List;

public class Game {
    private long id;
    private Sport sport;
    private Date date;
    private List<Referee> referees;
    private Team firstOpponent;
    private Team secondOpponent;
    private Address address;

    public long getId() {
        return id;
    }

    public Sport getSport() {
        return sport;
    }

    public Date getDate() {
        return date;
    }

    public List<Referee> getReferees() {
        return referees;
    }

    public Team getFirstOpponent() {
        return firstOpponent;
    }

    public Team getSecondOpponent() {
        return secondOpponent;
    }

    public Address getAddress() {
        return address;
    }
}
