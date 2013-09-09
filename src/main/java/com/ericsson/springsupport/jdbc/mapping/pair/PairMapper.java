package com.ericsson.springsupport.jdbc.mapping.pair;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PairMapper implements RowMapper<Pair> {

    public Pair mapRow(ResultSet resultSet, int paramInt) throws SQLException {
        return new Pair(resultSet.getString(1), resultSet.getString(2));
    }
}
