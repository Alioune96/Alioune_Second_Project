package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class TransferController {
    TransferDao transferDao;

    public TransferController(TransferDao transferDao){
        this.transferDao=transferDao;
    }



    @RequestMapping(path = "test2/{userId}", method = RequestMethod.GET)
    public Map<Integer,String>listofUser(@PathVariable int userId){
        return transferDao.listOf(userId);

    }

    @RequestMapping(path = "test/{id}", method = RequestMethod.GET)
    public List<Transfers> getPendingTransfers(@PathVariable int id){
        return transferDao.getTransfersByUserId(id);
    }


    @RequestMapping(path = "/transfers/{id}", method = RequestMethod.GET)
    public List<Transfers> getTransfersByUserId(@PathVariable int id) {
        return transferDao.getTransfersByUserId(id);
    }







}
