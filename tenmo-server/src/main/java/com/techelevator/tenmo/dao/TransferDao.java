package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.util.List;
import java.util.Map;

public interface TransferDao {
    //question 1 was complete



    //question 2 was complete

    //question 3 complete

    //question 4/
    // need two updates,
    Map<Integer,String> listOf(int userId);

    //question 5 // done
    List<Transfers> getTransfersByUserId(int id);

    //question 6 // done
    Transfers getTransfersById(int id);

    String sendToUser(int idFrom ,int idTo ,int amount);


    //question 8 //  done
    List<Transfers> getPendingTransfers(int id);

    //






}

