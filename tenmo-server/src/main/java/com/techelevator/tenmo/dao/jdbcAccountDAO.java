package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.client.RestTemplate;

public class jdbcAccountDAO implements AccountDao {
    private JdbcTemplate jdbcTemplate;

    public jdbcAccountDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public int getbalance(int accountId) {
        String hello = "SELECT balance FROM account WHERE account_id = ? ;\n";
        SqlRowSet balanceFromDataBase = jdbcTemplate.queryForRowSet(hello, accountId);
        if(balanceFromDataBase!=null){
            if(balanceFromDataBase.next()){
                return balanceFromDataBase.getInt("balance");
            }
        }
        return 0;
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



    public Account mapsToRow(SqlRowSet rowSet){
        Account account = new Account();
        account.setUser_id(rowSet.getInt("user_id"));
        account.setAccount_id(rowSet.getInt("account_id"));
        account.setBalance(rowSet.getBigDecimal("balance"));
        return account;
    }
}
