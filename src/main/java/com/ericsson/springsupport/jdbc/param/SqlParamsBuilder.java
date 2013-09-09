package com.ericsson.springsupport.jdbc.param;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.util.Assert;

public class SqlParamsBuilder extends MapSqlParameterSource {
    
    public SqlParamsBuilder() {}
    
    public static SqlParamsBuilder emptyParametersMap() {
        return new SqlParamsBuilder();
    }

    public SqlParamsBuilder addPair(String variable, Object value) {
        validateNotNull(variable);
        addValue(variable, value);
        return this;
    }

    public SqlParamsBuilder addPair(String variable, Object value, int sqlType) {
        validateNotNull(variable);
        addValue(variable, value, sqlType);
        return this;
    }

    private void validateNotNull(String variable) {
        Assert.notNull(variable, "Variable name must not be null.");
    }
}
