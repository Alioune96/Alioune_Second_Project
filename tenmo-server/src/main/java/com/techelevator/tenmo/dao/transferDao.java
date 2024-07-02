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
    //question 1 was complete

    //question 2 was complete

    //question 3 complete

    //question 4/
    // need two updates,
    Map<Integer,String> listOf();

    //question 5 // done
    List<Transfers> getTransfersByUserId(int id);

    //question 6 // done
    Transfers getTransfersById(int id);


    //question 8 //  done
    List<Transfers> getPendingTransfers(int id);

    //







}
