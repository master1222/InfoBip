package com.infobip.totorotournamentapi.repositories;

import com.infobip.totorotournamentapi.domains.Player;
import com.infobip.totorotournamentapi.exceptions.EtInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;


import java.sql.PreparedStatement;
import java.sql.Statement;

@Repository
public class PlayerRepositoryImplementation  implements  PlayerRepository{

    private static final String SQL_PLAYER_CREATE = "INSERT INTO ET_PLAYERS(PLAYER_ID, NAME ,AGE , SCORE) VALUES(NEXTVAL('ET_PLAYERS_SEQ'), ?, ?, ?)";

    private static final String SQL_FIND_BY_ID = "SELECT PLAYER_ID, NAME, AGE, SCORE FROM ET_PLAYERS WHERE PLAYER_ID = ?";
    private static final String SQL_PLAYERS_COUNT = "SELECT COUNT(*) FROM ET_PLAYERS";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Integer create(String name, Integer age) throws EtInputException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(SQL_PLAYER_CREATE, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1,name);
                ps.setInt(2,age);
                ps.setInt(3,0);
                return  ps;
            },keyHolder);
            return (Integer) keyHolder.getKeys().get("PLAYER_ID");
        }catch (Exception e){
            throw new EtInputException("Invalid player details!");
        }
    }

    @Override
    public Player findById(Integer playerId) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new Object[]{playerId}, playerRowMapper);
    }

    @Override
    public Integer getPlayersCount() {
        Integer i = jdbcTemplate.queryForObject(SQL_PLAYERS_COUNT,new Object[]{},Integer.class);
        return  i;
    }

    private RowMapper<Player> playerRowMapper = ((resultSet, i) -> {
        return new Player(resultSet.getInt("PLAYER_ID"),
                resultSet.getString("NAME"),
                resultSet.getInt("AGE"),
                resultSet.getInt("SCORE"));
    });
}
