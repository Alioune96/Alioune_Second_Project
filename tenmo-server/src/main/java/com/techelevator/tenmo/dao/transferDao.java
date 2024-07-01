package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.data.relational.core.sql.In;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public interface transferDao {


    //question 4/
    // need two updates,
    Map<Integer,String> listOf();

    //question 5
    List<Transfers> getTransfers();

    //question 6
    Transfers getTransfersById(int id);


    //question 8 //inner join from status to transfer to account
    List<Transfers> getPendingTransfers(int id);





}
