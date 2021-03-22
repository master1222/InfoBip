package com.infobip.totorotournamentapi.services;

import com.infobip.totorotournamentapi.domains.Match;
import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;

import java.util.List;

public interface TournamentService {

    List<Match> drawTournament() throws EtInputException;

    void setResult(Integer matchId, String result) throws  EtInputException;

    List<Player> getWinners() throws  EtInputException;

}
