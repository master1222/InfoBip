package com.infobip.totorotournamentapi.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Player {

    private Integer playerId;
    private String name;
    @JsonIgnore
    private Integer age;
    @JsonIgnore
    private Integer score;


    public Player(Integer playerId, String name, Integer age, Integer score) {
        this.name = name;
        this.playerId = playerId;
        this.age = age;
        this.score = score;
    }

    public Player() {

    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
