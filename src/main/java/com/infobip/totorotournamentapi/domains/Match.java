package com.infobip.totorotournamentapi.domains;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Match {

    private Integer matchId;
    private Integer firstPlayer;
    private Integer secondPlayer;
    @JsonIgnore
    private String result;

    public Match(Integer matchId, Integer firstPlayer, Integer secondPlayer, String result) {
        this.matchId = matchId;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.result = result;
    }

    public Integer getMatchId() {
        return matchId;
    }

    public void setMatchId(Integer matchId) {
        this.matchId = matchId;
    }

    public Integer getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(Integer firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public Integer getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(Integer secondPlayer) {
        this.secondPlayer = secondPlayer;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
