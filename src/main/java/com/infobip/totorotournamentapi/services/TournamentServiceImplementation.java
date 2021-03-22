package com.infobip.totorotournamentapi.services;

import com.infobip.totorotournamentapi.domains.Match;
import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import com.infobip.totorotournamentapi.repositories.PlayerRepository;
import com.infobip.totorotournamentapi.repositories.TournamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TournamentServiceImplementation implements TournamentService {

    @Autowired
    TournamentRepository tournamentRepository;
    PlayerRepository playerRepository;


    //Drawing of tournament. no inputs are required
    @Override
    public List<Match> drawTournament() throws EtInputException {
        return tournamentRepository.draw();
    }

    //Update result of match
    @Override
    public void setResult(Integer matchId, String result) throws EtInputException {
        tournamentRepository.updateMatchResult(matchId,result);
    }

    //Get list of players with the highest score.
    // Tournament Repository is returning List of map, because of that conversion is necessary.
    @Override
    public List<Player> getWinners() throws EtInputException {
        List<Map<String,Object>> playersMap = tournamentRepository.findByScore();
        List<Player> players = new ArrayList<Player>();
        for ( Map<String,Object> playerMap: playersMap){
            Player player = new Player();
            player.setPlayerId((Integer) playerMap.get("PLAYER_ID"));
            player.setName((String) playerMap.get("NAME"));
            players.add(player);
        }
        return players;
    }
}
