package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface transferDao {


    //question 4/
    // need two updates,
    Transfers updateSendTransfer(int fromId, int toId, BigDecimal balance);

    //question 5
    List<Transfers> getTransfers();

    //question 6
    Transfers getTransfersById(int id);


    //question 8 //inner join from status to transfer to account
    List<Transfers> getPendingTransfers(int id);





}
