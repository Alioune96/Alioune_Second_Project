package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {
    int getbalance(int accountId);
    Account getAccountById(int id);
}
