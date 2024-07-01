package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;

import java.math.BigDecimal;
import java.util.List;

public interface transferDao {
    //question 1 was complete

    //question 2 was complete

    //question 3 complete

    //question 4/
    // need two updates,
    Transfers updateSendTransfer(int fromId, int toId, BigDecimal balance);

    //question 5 // done
    List<Transfers> getTransfers();

    //question 6 // done
    Transfers getTransfersById(int id);


    //question 8 //  done
    List<Transfers> getPendingTransfers(int id);

    //







}
