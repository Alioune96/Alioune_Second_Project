package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

@Component
public class JdbcTransfersDao implements TransferDao {

  private JdbcTemplate jdbcTemplate;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
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

    @Override
    public List<Transfers> getTransfers() {
        return null;
    }

    @Override
    public Transfers getTransfersById(int id) {
        return null;
    }

    @Override
    public List<Transfers> getPendingTransfers(int id) {
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
