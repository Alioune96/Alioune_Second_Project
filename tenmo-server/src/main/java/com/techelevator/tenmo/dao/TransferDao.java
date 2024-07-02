package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfers;

import java.util.List;
import java.util.Map;

public interface TransferDao {


    //question 4/
    // need two updates,
    Map<Integer,String> listOf(int userId);

    //question 5
    List<Transfers> getTransfers();

    //question 6
    Transfers getTransfersById(int id);


    //question 8 //inner join from status to transfer to account
    List<Transfers> getPendingTransfers(int id);





}
