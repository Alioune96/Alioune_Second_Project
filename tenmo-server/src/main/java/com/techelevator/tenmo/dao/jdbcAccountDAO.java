package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.web.client.RestTemplate;

public class jdbcAccountDAO implements AccountDao {
    JdbcTemplate jdbcTemplate;

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
}
