package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfers;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JdbcTransfersDao implements TransferDao {

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
        List <Transfers> transfers = new ArrayList<>();

        String sql = "SELECT t.transfer_id,  t.account_from,  t.account_to,  t.amount,afu.username AS from_username,atu.username AS to_username " +
                "FROM transfer t " +
                "JOIN account af ON t.account_from = af.account_id " +
                "JOIN tenmo_user afu ON af.user_id = afu.user_id " +
                "JOIN account at ON t.account_to = at.account_id " +
                "JOIN tenmo_user atu ON at.user_id = atu.user_id " +
                "WHERE af.user_id = ? OR at.user_id = ? " +
                " ORDER BY t.transfer_id ;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
            while (results.next()) {
                Transfers transfer = mapToTransferSet(results);
                transfers.add(transfer);
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
    public Map<Integer,String> listOf(int userId) {
        Map<Integer, String> tenmoUser = new HashMap<>();
        String sqlForprint = "SELECT user_id, username FROM tenmo_user WHERE user_id != ?;";

        SqlRowSet rowToPrint = jdbcTemplate.queryForRowSet(sqlForprint,userId);
        if (!rowToPrint.wasNull()) {
            while (rowToPrint.next()) {
                String capFirstLetter = rowToPrint.getString("username").substring(0,1).toUpperCase();
                String remaindingLetter = rowToPrint.getString("username").substring(1);
                String value = capFirstLetter+remaindingLetter;
                System.out.println(capFirstLetter);
                tenmoUser.put(rowToPrint.getInt("user_id"), value);
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
        transfers.setFromUsername(sqlRowSet.getString("from_username"));
        transfers.setToUsername(sqlRowSet.getString("to_username"));
        return transfers;

    }
}
