package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDAO implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getbalance(int userId) {
        String hello = "SELECT a.balance FROM account AS a\n" +
                "JOIN tenmo_user as tu ON a.user_id = tu.user_id\n" +
                "WHERE tu.user_id = ?;";
        SqlRowSet balanceFromDataBase = jdbcTemplate.queryForRowSet(hello, userId);
        if(balanceFromDataBase!=null){
            if(balanceFromDataBase.next()){
                return balanceFromDataBase.getBigDecimal("balance");
            }
        }
        return BigDecimal.valueOf(0);
    }
    @Override
    public Account getAccountById(int id) {
        Account accountToReturn = null;
        String statement = "SELECT * FROM account WHERE account_id = ?;";
        SqlRowSet sqlvalue = jdbcTemplate.queryForRowSet(statement, id);
        if (!sqlvalue.wasNull()){
            if(sqlvalue.next()){
                return accountToReturn = mapsToRow(sqlvalue);
            }
        }
        return null;
    }
//this is returning the balance of account
    @Override
    public BigDecimal getAllAccount() {
        String all = "SELECT * FROM account;";
        SqlRowSet here = jdbcTemplate.queryForRowSet(all);
        if(!here.wasNull()){
            if(here.next()){
                return here.getBigDecimal("balance");

            }
        }
        return BigDecimal.valueOf(0);
    }


    public Account mapsToRow(SqlRowSet rowSet){
        Account account = new Account();
        account.setUser_id(rowSet.getInt("user_id"));
        account.setAccount_id(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
