package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransfersDao implements transferDao {
    private JdbcTemplate jdbcTemplate;
    private Logger log = LoggerFactory.getLogger(getClass());

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfers updateSendTransfer(int fromId, int toId, BigDecimal balance) {
        return null;
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
    public List<Transfers> getTransfers(){
        List<Transfers> transfers = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer ;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfers.getTransferId(), transfers.getTransferTypeId(), transfers.getTransferStatusId(),  transfers.getAccountFrom() ,transfers.getAccountTo(), transfers.getAmount());
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
    public List<Transfers> getPendingTransfers(int id) {
        List<Transfers> transfers = null;
        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, account_to, amount " +
                "FROM transfer AS t" +
                "JOIN transfer_status AS ts ON ts.transfer_status_id = t.transfer_status_id; " +
                "WHERE t.transfer_id = ?  AND  ts.transfer_status_id = 1;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfers.getTransferId(), transfers.getTransferTypeId(), transfers.getTransferStatusId(),  transfers.getAccountFrom() ,transfers.getAccountTo(), transfers.getAmount());
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





}
