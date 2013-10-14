package com.ericsson.springsupport.jdbc.mapping.map;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public interface RowInterceptor<K, V> {
    void interceptRow(ResultSet row, Map<K, V> resultMap) throws SQLException;
}
