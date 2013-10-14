package com.ericsson.springsupport.jdbc.mapping.map;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

public class MapExtractor<K, V> implements ResultSetExtractor<Map<K, V>> {
    private final RowInterceptor<K, V> rowInterceptor;

    public MapExtractor(RowInterceptor<K, V> rowInterceptor) {
        this.rowInterceptor = rowInterceptor;
    }

    public Map<K, V> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
        Map<K, V> map = new LinkedHashMap<K, V>();
        while (resultSet.next()) {
            rowInterceptor.interceptRow(resultSet, map);
        }
        return map;
    }
}