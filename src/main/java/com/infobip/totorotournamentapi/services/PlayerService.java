package com.infobip.totorotournamentapi.services;

import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;

import java.util.List;

public interface PlayerService {

    Player addPlayer(String name,Integer age) throws EtInputException;

}
