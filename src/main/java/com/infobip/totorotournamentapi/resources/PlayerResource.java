package com.infobip.totorotournamentapi.resources;


import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import com.infobip.totorotournamentapi.services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;


@RestController

public class PlayerResource {

    @Autowired
    PlayerService playerService;

    @PostMapping("/players")
    public ResponseEntity<Player> addUser(@RequestBody Map<String, Object> playerMap) {
        String name = (String) playerMap.get("name");
        Integer age = (Integer) playerMap.get("age");
        Map<String, String> map = new HashMap<>();
        try {
            Player player = playerService.addPlayer(name, age);
            return new ResponseEntity<>(player,HttpStatus.OK);
        } catch (EtInputException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

}
