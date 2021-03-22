package com.infobip.totorotournamentapi.services;

import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import com.infobip.totorotournamentapi.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PlayerServiceImplementation implements  PlayerService{

    @Autowired
    PlayerRepository playerRepository;

    // Add player service - check if inputs are not empty and count of players is not bigger than is allowed (Total count is 6)
    @Override
    public Player addPlayer(String name, Integer age) throws  EtInputException{

            if (name == null || name.equals("") || age < 0){
                throw new EtInputException("Incorrect input!");
            }
            if (playerRepository.getPlayersCount() > 5){
                throw new EtInputException("Max count of players is limited to 6");
            }
            Integer playerId = playerRepository.create(name,age);
            return playerRepository.findById(playerId);
    }

}