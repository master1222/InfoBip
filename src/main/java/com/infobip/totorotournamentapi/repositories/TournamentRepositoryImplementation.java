package com.infobip.totorotournamentapi.repositories;

import com.infobip.totorotournamentapi.domains.Match;
import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Repository
public class TournamentRepositoryImplementation implements TournamentRepository {

    private static final String SQL_GET_PLAYERS = "SELECT PLAYER_ID FROM ET_PLAYERS";
    private static final String SQL_MATCH_BY_ID = "SELECT MATCH_ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, RESULT FROM ET_MATCHES WHERE MATCH_ID = ?";
    private static final String SQL_GET_WINNER = "SELECT PLAYER_ID, NAME FROM ET_PLAYERS GROUP BY PLAYER_ID HAVING SCORE = ( SELECT MAX (SCORE) FROM ET_PLAYERS )";
    private static final String SQL_INSERT_MATCH = "INSERT INTO ET_MATCHES(MATCH_ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID, RESULT) VALUES(NEXTVAL('ET_MATCHES_SEQ'),?,?,?)";
    private static final String SQL_UPDATE_MATCH = "UPDATE ET_MATCHES SET RESULT = ? WHERE MATCH_ID = ?";
    private static final String SQL_UPDATE_SCORE = "UPDATE ET_PLAYERS SET SCORE = ? WHERE PLAYER_ID = ?";
    private static final String SQL_GET_MATCHES = "SELECT MATCH_ID, FIRST_PLAYER_ID, SECOND_PLAYER_ID FROM ET_MATCHES";
    private static final String SQL_FIND_BY_ID = "SELECT PLAYER_ID, NAME, AGE, SCORE FROM ET_PLAYERS WHERE PLAYER_ID = ?";
    private static final int WINNER_POINTS = 3;
    private static final int LOOSER_POINTS = 1;


    @Autowired
    JdbcTemplate jdbcTemplate;
    //PlayerRepository playerRepository;

    @Override
    public List<Match> draw() {
        List<Map<String, Object>> players = this.getPlayers();
        if (players.size() < 4 || players.size()%2==1) throw new EtInputException("Not enought players for draw!");
        List<Match> matches = this.generateMatches(this.getPlayers());
        this.jdbcTemplate.batchUpdate(
                SQL_INSERT_MATCH,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, matches.get(i).getFirstPlayer());
                        ps.setInt(2, matches.get(i).getSecondPlayer());
                        ps.setString(3, matches.get(i).getResult());
                    }

                    @Override
                    public int getBatchSize() {
                        return matches.size();
                    }
                }
        );
        return getMatches();
    }

    @Override
    public List<Map<String, Object>> findByScore() {
        return jdbcTemplate.queryForList(SQL_GET_WINNER);
    }

    @Override
    public Match findById(Integer MatchId) {
        try {
            return jdbcTemplate.queryForObject(SQL_MATCH_BY_ID, new Object[]{MatchId}, matchRowMapper);
        }catch (EmptyResultDataAccessException e){
            throw new EtInputException("Wrong Id of match");
        }
    }

    @Override
    public List<Map<String, Object>> getPlayers() {
        return jdbcTemplate.queryForList(SQL_GET_PLAYERS);
    }

    @Override
    public void updateMatchResult(Integer matchId, String result) {
        Match match = this.findById(matchId);
        Player firstPlayer = findPlayerById(match.getFirstPlayer());
        Player secondPlayer = findPlayerById(match.getFirstPlayer());

        if (isFirstPlayerWinner(result)) {
            updatePlayerScore(firstPlayer.getPlayerId(), firstPlayer.getScore() + WINNER_POINTS);
            updatePlayerScore(secondPlayer.getPlayerId(), secondPlayer.getScore() + LOOSER_POINTS);
        } else {
            updatePlayerScore(firstPlayer.getPlayerId(), firstPlayer.getScore() + LOOSER_POINTS);
            updatePlayerScore(secondPlayer.getPlayerId(), secondPlayer.getScore() + WINNER_POINTS);
        }

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_MATCH);
                ps.setString(1, result);
                ps.setInt(2, matchId);
                return ps;
            });
        } catch (Exception e) {
            throw new EtInputException("Error while updating match!");
        }
    }

    @Override
    public void updatePlayerScore(Integer playerId, Integer score) {

        try {
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_SCORE);
                ps.setInt(1, score);
                ps.setInt(2, playerId);
                return ps;
            });
        } catch (Exception e) {
            throw new EtInputException("Error while updating score!");
        }
    }

    @Override
    public Player findPlayerById(Integer playerId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{playerId}, playerRowMapper);
    }

    private RowMapper<Match> matchRowMapper = ((rs, rowNum) -> {
        return new Match(rs.getInt("MATCH_ID"),
                rs.getInt("FIRST_PLAYER_ID"),
                rs.getInt("SECOND_PLAYER_ID"),
                rs.getString("RESULT"));
    });

    private RowMapper<Player> playerRowMapper = ((resultSet, i) -> {
        return new Player(resultSet.getInt("PLAYER_ID"),
                resultSet.getString("NAME"),
                resultSet.getInt("AGE"),
                resultSet.getInt("SCORE"));
    });

    private List<Match> getMatches(){
        return jdbcTemplate.query(SQL_GET_MATCHES,
                new RowMapper<Match>(){
                    @Override
                    public Match mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Match match = new Match(rs.getInt("MATCH_ID"),
                                                rs.getInt("FIRST_PLAYER_ID"),
                                                rs.getInt("SECOND_PLAYER_ID"),"");
                        return match;
                    };
                });
    }


    private List<Match> generateMatches(List<Map<String, Object>> players) {
        List<Match> matches = new ArrayList<Match>();
        Collections.shuffle(players);
        for (int i = 0; i < players.size(); i++) {
            Integer playerId = (Integer) players.get(i).get("PLAYER_ID");
            Integer opponentId = (Integer) players.get((i + 1) % players.size()).get("PLAYER_ID");
            matches.add(new Match(0, playerId, opponentId, ""));
        }
        for (int i = 0; i < players.size()/2; i++) {
            Integer playerId = (Integer) players.get(i).get("PLAYER_ID");
            Integer opponentId = (Integer) players.get(players.size()/2+i).get("PLAYER_ID");
            matches.add(new Match(0, playerId, opponentId, ""));
        }
        return matches;
}
    private boolean isFirstPlayerWinner(String result) {
        try {
            String[] results = result.split(":");
            if (Integer.parseInt(results[0]) > Integer.parseInt(results[1])) return true;
            return false;
        } catch (Exception e) {
            throw new EtInputException("Wrong result format!");
        }
    }
}
