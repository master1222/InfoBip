package com.infobip.totorotournamentapi.repositories;

import com.infobip.totorotournamentapi.domains.Player;

public interface PlayerRepository {

    Integer create(String name, Integer age);

    Player findById(Integer playerId);

    Integer getPlayersCount();

}
