package com.infobip.totorotournamentapi.resources;


import com.infobip.totorotournamentapi.domains.Match;
import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import com.infobip.totorotournamentapi.services.TournamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tournament")
public class TournamentResource {

    @Autowired
    TournamentService tournamentService;

    //GET request handler - get list of matches
    @RequestMapping("/draw")
    public ResponseEntity<Map<String, List<Match>>> drawTournament() {
        try {
            List<Match> matches = tournamentService.drawTournament();
            Map<String, List<Match>> map = new HashMap<String, List<Match>>();
            map.put("matches", matches);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (EtInputException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    // PUT request handler - update result of match
    @PutMapping("/results/{matchId}")
    public ResponseEntity<Map<String, String>> updateScore(@PathVariable(value = "matchId") Integer matchId, @RequestBody Map<String, Object> match) {
        String result = (String) match.get("result");
        Map<String, String> map = new HashMap<>();
        try {
            tournamentService.setResult(matchId, result);
            map.put("message", "Score registered");
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (EtInputException e) {
            map.put("message", "Invalid score provided");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    }

    //GET request handler - get list of winners
    @RequestMapping("winner")
    public ResponseEntity<Map<String, List<Player>>> getWinners() {
        try {
            List<Player> winners = tournamentService.getWinners();
            Map<String, List<Player>> map = new HashMap<String, List<Player>>();
            map.put("winners", winners);
            return new ResponseEntity<>(map, HttpStatus.OK);
        } catch (EtInputException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }
}


