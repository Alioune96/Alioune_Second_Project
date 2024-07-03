package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "test2" , method = RequestMethod.POST)
    public String sendToUser(@RequestBody Transfers newTransfer){
        return transferDao.sendToUser(newTransfer);

    }

    @RequestMapping(path = "test2/tryThis", method = RequestMethod.POST)
    public String sendRequest(@RequestBody Transfers transferRequest){
        return transferDao.confirmation(transferRequest);
    }


}
