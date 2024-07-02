package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class JdbcTransfersDao implements transferDao {

    private JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(getClass());

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Transfers  getTransfersById(int id){
        Transfers transfers = null;
        String sql = "SELECT * " +
                "FROM transfer " +
                "WHERE transfer_id = ?";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                transfers = mapToTransferSet(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;
    }
    @Override
    public List<Transfers> getTransfersByUserId(int id){
        List<Transfers> transfers = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer" +
                "where account_from = ?;" ;
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            if (results.next()) {
                transfers.add(mapToTransferSet(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;
    }



    @Override
    public List<Transfers> getPendingTransfers(int id) {
        List<Transfers> transfers = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer AS t" +
                "JOIN transfer_status AS ts ON ts.transfer_status_id = t.transfer_status_id; " +
                "WHERE t.transfer_id = ?  AND  ts.transfer_status_id = 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
            while (results.next()) {
                transfers.add(mapToTransferSet(results));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return transfers;

    }

    @Override
    public Map<Integer,String> listOf() {
        Map<Integer, String> tenmoUser = new HashMap<>();
        String sqlForprint = "SELECT user_id, username FROM tenmo_user;";

        SqlRowSet rowToPrint = jdbcTemplate.queryForRowSet(sqlForprint);
        if (!rowToPrint.wasNull()) {
            while (rowToPrint.next()) {
                tenmoUser.put(rowToPrint.getInt("user_id"), rowToPrint.getString("username"));
            }
           return tenmoUser;


        }
return null;
    }


    public Transfers mapToTransferSet(SqlRowSet sqlRowSet){
        Transfers transfers = null;
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfers.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfers.setAccountTo(sqlRowSet.getInt("account_to"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        return transfers;

    }
}
