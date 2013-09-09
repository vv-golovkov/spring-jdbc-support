package com.ericsson.springsupport.jdbc;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;

import com.ericsson.springsupport.jdbc.datasource.DataSourceAware;
import com.ericsson.springsupport.jdbc.datasource.DataSourceProvider;
import com.ericsson.springsupport.jdbc.exception.IncorrectResultDataException;
import com.ericsson.springsupport.jdbc.logging.SmartLogger;
import com.ericsson.springsupport.jdbc.mapping.pair.Pair;
import com.ericsson.springsupport.jdbc.mapping.pair.PairMapper;
import com.ericsson.springsupport.jdbc.param.SqlParamsBuilder;

public class SpringJdbcSupport extends DataSourceProvider {
    private static final SmartLogger logger = SmartLogger.getLogger(SpringJdbcSupport.class);

    public SpringJdbcSupport(String providerUrl, String contextFactory, String jndi) {
        super(providerUrl, contextFactory, jndi);
    }

    public SpringJdbcSupport(DataSourceAware customDataSource) {
        super(customDataSource);
    }

    public SqlParamsBuilder getSqlParamsBuilder() {
        return new SqlParamsBuilder();
    }

    /*************** query for PAIR ***************/
    public Pair queryForPair(String sql) {
        List<Pair> pairs = queryForPairs(sql);
        return handleSingleResult(pairs);
    }

    public List<Pair> queryForPairs(String sql) {
        logger.info("14 *************** queryForPairs without params is working ***************");
        return getNamedParameterJdbcTemplate().query(sql, SqlParamsBuilder.emptyParametersMap(), new PairMapper());
    }

    public Pair queryForPair(String sql, SqlParameterSource parameters) {
        List<Pair> pairs = queryForPairs(sql, parameters);
        return handleSingleResult(pairs);
    }

    public List<Pair> queryForPairs(String sql, SqlParameterSource parameters) {
        return getNamedParameterJdbcTemplate().query(sql, parameters, new PairMapper());
    }

    /*************** query for MAPPER ***************/
    public <T> T queryForWrappedObject(String sql, RowMapper<T> rowMapper) {
        List<T> rows = queryForWrappedList(sql, rowMapper);
        return handleSingleResult(rows);
    }

    public <T> List<T> queryForWrappedList(String sql, RowMapper<T> rowMapper) {
        logger.info("12 *************** queryForWrappedList without params is working ***************");
        return getNamedParameterJdbcTemplate().query(sql, SqlParamsBuilder.emptyParametersMap(), rowMapper);
    }

    public <T> T queryForWrappedObject(String sql, SqlParameterSource parameters, RowMapper<T> rowMapper) {
        List<T> rows = queryForWrappedList(sql, parameters, rowMapper);
        return handleSingleResult(rows);
    }

    public <T> List<T> queryForWrappedList(String sql, SqlParameterSource parameters, RowMapper<T> rowMapper) {
        return getNamedParameterJdbcTemplate().query(sql, parameters, rowMapper);
    }

    /*************** query for LIST ***************/
    public <T> List<T> queryForList(String sql, SqlParameterSource parameters, Class<T> returnType) {
        return getNamedParameterJdbcTemplate().queryForList(sql, parameters, returnType);
    }

    public <T> List<T> queryForList(String sql, Class<T> returnType) {
        return getNamedParameterJdbcTemplate().queryForList(sql, SqlParamsBuilder.emptyParametersMap(), returnType);
    }

    /*************** query for OBJECT ***************/
    public <T> T queryForObject(String sql, SqlParameterSource parameters, Class<T> returnType) {

        // spring source code for "queryForObject(...) method":
        // size == 0 -> throw new EmptyResultDataAccessException(1);
        // size > 1 -> throw new IncorrectResultSizeDataAccessException(1, size);

        List<T> resultList = queryForList(sql, parameters, returnType);
        return handleSingleResult(resultList);
    }

    public <T> T queryForObject(String sql, Class<T> returnType) {
        List<T> resultList = queryForList(sql, returnType);
        return handleSingleResult(resultList);
    }

    /*************** PROCEDURE and FUNCTION ***************/
    public Map<String, Object> callProcedure(String procedureName, SqlParameterSource parameters) {
        return getJdbcCallWithProcedureSupport(procedureName).execute(parameters);
    }

    public <T> T callFunction(String functionName, SqlParameterSource parameters, Class<T> returnType) {
        logger.info("1 *************** callFunction with params is working ***************");
        return getJdbcCallWithFunctionSupport(functionName).executeFunction(returnType, parameters);
    }

    /*************** UPDATE, DELETE, INSERT, CREATE ***************/
    public int executeUpdate(String sql, SqlParameterSource parameters) {
        return getNamedParameterJdbcTemplate().update(sql, parameters);
    }

    public int executeUpdate(String sql) {
        return getNamedParameterJdbcTemplate().update(sql, SqlParamsBuilder.emptyParametersMap());
    }

    public int executeInsert(String sql, SqlParameterSource parameters) {
        return executeUpdate(sql, parameters);
    }

    public int executeInsert(String sql) {
        return executeUpdate(sql);
    }

    public int executeDelete(String sql, SqlParameterSource parameters) {
        return executeUpdate(sql, parameters);
    }

    public int executeDelete(String sql) {
        return executeUpdate(sql);
    }

    public void executeCreate(String sql) {
        getJdbcTemplate().execute(sql);
    }

    /********************************** privates ***********************************/
    
    private SimpleJdbcCall getJdbcCallWithProcedureSupport(String procedureName) {
        return getSimpleJdbcCall().withProcedureName(procedureName);
    }

    private SimpleJdbcCall getJdbcCallWithFunctionSupport(String functionName) {
        return getSimpleJdbcCall().withFunctionName(functionName);
    }

    private SimpleJdbcCall getSimpleJdbcCall() {
        return new SimpleJdbcCall(getDataSource());
    }

    private NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(getDataSource());
    }

    private JdbcTemplate getJdbcTemplate() {
        return new JdbcTemplate(getDataSource());
    }

    private <T> T handleSingleResult(List<T> list) {
        int size = list.size();
        if (size == 0) {
            return null;
        }
        if (size > 1) {
            String message = String.format("Query for object returns more then 1 row - %d", size);
            logger.info(message);
            throw new IncorrectResultDataException(message);
        }
        return list.get(0);
    }
}
