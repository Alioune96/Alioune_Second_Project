package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class TransferController {
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;



    @RequestMapping(path = "test2/{userId}", method = RequestMethod.GET)
    public Map<Integer,String>listofUser(@PathVariable int userId){
        return transferDao.listOf(userId);

    }




    @RequestMapping(path = "transfers/{id}", method = RequestMethod.GET)
    public List<Transfers> getTransfersByUserId(@PathVariable int id) {
        return transferDao.getTransfersByUserId(id);
    }

    @RequestMapping(path = "transfers/pending", method = RequestMethod.GET)
    public List<Transfers> getPendingTransfers(@RequestParam(required = false) Integer pendingId) {
        return transferDao.getPendingTransfers(pendingId);
    }

    @RequestMapping(path = "test2" , method = RequestMethod.POST)
    public String sendToUser(@RequestBody Transfers newTransfer){
        return transferDao.sendToUser(newTransfer);

    }







}
