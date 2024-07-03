package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {
    @Autowired
    TransferDao transferDao;
    @Autowired
    UserDao userDao;




// no id's in the path


    @RequestMapping(path = "/listUsers", method = RequestMethod.GET)
    public Map<Integer,String>listofUser(Principal newP){
        return transferDao.listOf(userDao.getUserByUsername(newP.getName()).getId());
    }


    @RequestMapping(path = "transfers/myTransfers", method = RequestMethod.GET)
    public List<Transfers> getTransfersByUserId(Principal newP) {
        return transferDao.getTransfersByUserId(userDao.getUserByUsername(newP.getName()).getId());
    }

    @RequestMapping(path = "transfer/pending", method = RequestMethod.GET)
    public List<Transfers> getPendingTransfers(Principal newP) {
        return transferDao.getPendingTransfers( userDao.getUserByUsername(newP.getName()).getId());

    }

    @RequestMapping(path = "transfer" , method = RequestMethod.POST)
    public String sendToUser(@RequestBody Transfers newTransfer){
        return transferDao.sendToUser(newTransfer);

    }

    @RequestMapping(path = "transfer/request", method = RequestMethod.POST)
    public String sendRequest(@RequestBody Transfers transferRequest){
        return transferDao.confirmation(transferRequest);
    }







}
