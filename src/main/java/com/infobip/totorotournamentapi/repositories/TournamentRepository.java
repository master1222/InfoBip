package com.infobip.totorotournamentapi.repositories;

import com.infobip.totorotournamentapi.domains.Match;
import com.infobip.totorotournamentapi.domains.Player;

import java.util.List;
import java.util.Map;

public interface TournamentRepository {


    List<Match> draw();

    List<Map<String, Object>> findByScore();

    Match findById(Integer MatchId);

    void updatePlayerScore(Integer playerId, Integer score);

    List<Map<String, Object>> getPlayers();

    void updateMatchResult(Integer matchId, String result);

    Player findPlayerById(Integer playerId);
}
