package com.company;

public abstract class Player {
    private boolean vote = false;
    private boolean day = true;
    private String id;
    private String role;


    public void setId(String id) {
        this.id = id;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setDay(boolean day) {
        this.day = day;
    }

    public void setVote(boolean vote) {
        this.vote = vote;
    }

    public String getId() {
        return id;
    }

    public String getRole() {
        return role;
    }

    boolean getVote() {
        return vote;
    }

    boolean getDay() {
        return day;
    }

    abstract boolean choose();


}
