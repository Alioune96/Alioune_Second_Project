package com.techelevator.tenmo.dao;

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

    JdbcTemplate jdbcTemplate;

    public JdbcTransfersDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Transfers updateSendTransfer(Account account, Scanner scanner, int fromUserId) {
        Map<Integer,String>tenmoUser = new HashMap<>();
        String sqlForprint = "SELECT user_id, username FROM tenmo_user;";
        int parseNumber = 0;
        int amountToSend = 0;
        SqlRowSet rowToPrint = jdbcTemplate.queryForRowSet(sqlForprint);
        if(!rowToPrint.wasNull()){
            while (rowToPrint.next()){
                tenmoUser.put(rowToPrint.getInt("user_id"), rowToPrint.getString("username"));
            }
            System.out.println(tenmoUser.entrySet());

            System.out.println("Who would you like to send money to?");
            boolean isthisValidNumber = true;
            try {
                String userAnswer = scanner.next();
                while (isthisValidNumber){
                    parseNumber = Integer.parseInt(userAnswer);
                    isthisValidNumber=false;
                }
            }catch (NumberFormatException e){
                System.out.println("Please provide an Valid User ID");
                scanner.nextLine();}
        }
        System.out.println("Who much would you like to send?");
        boolean amountisValid = true;
        while (amountisValid) {
            try {
                String updateFrom = scanner.next();
                amountToSend = Integer.valueOf(updateFrom);
                amountisValid=false;

            } catch (NumberFormatException e){
                System.out.println("Please provide us with an valid number");
            }
        }
        String updateFromAccount = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        jdbcTemplate.update(updateFromAccount,amountToSend,account.getAccount_id());
        String sendAccount = "SELECT account_id WHERE account_id ?;";
        SqlRowSet findOne = jdbcTemplate.queryForRowSet(sendAccount);
        String updateToAccount = "UPDATE account set balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(updateToAccount, amountToSend, )

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


    public Transfers mapToTransferSet(SqlRowSet sqlRowSet ){
        Transfers transfers = null;
        transfers.setTransferId(sqlRowSet.getInt("transfer_id"));
        transfers.setTransferStatusId(sqlRowSet.getInt("transfer_status_id"));
        transfers.setAccountFrom(sqlRowSet.getInt("account_from"));
        transfers.setAccountTo(sqlRowSet.getInt("account_to"));
        transfers.setAmount(sqlRowSet.getBigDecimal("amount"));
        return transfers;

    }
}
