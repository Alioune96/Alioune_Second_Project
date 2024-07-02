package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    BigDecimal getbalance(int userId);
    Account getAccountById(int id);

    BigDecimal getAllAccount ();
}
