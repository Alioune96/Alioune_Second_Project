package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.TransferDao;
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(path = "test2?idFrom=&idTo=&amount=", method = RequestMethod.POST)
    public String sendToUser(@RequestParam int idFrom ,@RequestParam int idTo , @RequestParam int amount){
        return transferDao.sendToUser(idFrom,idTo,amount);
    }


}
