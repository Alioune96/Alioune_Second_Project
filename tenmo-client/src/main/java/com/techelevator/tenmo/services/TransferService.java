package com.techelevator.tenmo.services;

import com.techelevator.tenmo.App;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfers;
import com.techelevator.tenmo.model.User;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TransferService {




    private final RestTemplate resttemplate = new RestTemplate();

    private static final String API_BASE_URL = "http://localhost:8080/";
    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AuthenticatedUser currentUser;

    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void setAuthenticatedUser(AuthenticatedUser currentUser) {
        this.currentUser = currentUser;
    }


    public String sendMoneyToUser(AuthenticatedUser currentUser, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL, BigDecimal accountbalance){
        int currentUserId = currentUser.getUser().getId();
        String resulted = "";
        boolean UserTrue = true;
        double here = 0.00;
        BigDecimal amountToSend = BigDecimal.valueOf(here);
        int userIDtosend = 0;
        while (UserTrue) {
            int userId = currentUser.getUser().getId();

            System.out.println("Current balance is: " + accountbalance);
            ResponseEntity<Map> response = resttemplate.exchange(API_BASE_URL,HttpMethod.GET,makeAuthEntity(),Map.class);
            Map<Integer, String> mapOfUser = response.getBody();
            String displayUser = "";
            for (int i = 0; i < 1; i++) {
                displayUser += mapOfUser.entrySet();
                displayUser += "\t";
                String newDisplay = displayUser.replace("=", "    ");
                String replaceAgain = newDisplay.replace(",", "\n");
                String oneLast = replaceAgain.replace("[", " ");
                String oneLastOne = oneLast.replace("]", " ");
                System.out.println("----------------------------------------------");
                System.out.println(" Users\n  Id" + "      name");
                System.out.println(oneLastOne);
                System.out.println("------\n" + "\n");
                displayUser = "";
            }

            System.out.print("Enter ID of user you are sending to (0) to cancel: ");
            String sendId = consoleService.getScanner().nextLine();
            int parseBefore = Integer.valueOf(sendId);
            if(parseBefore==0){
                return "Good-Bye";
            }
            System.out.print("Enter amount: ");
            String userAmount = consoleService.getScanner().nextLine();
            boolean isValidNumber = true;
            while (isValidNumber) {

                    try {
                        BigDecimal amountToSendAgain  = new BigDecimal(userAmount);
                        amountToSend=amountToSendAgain;
                        userIDtosend = Integer.parseInt(sendId);
                        isValidNumber=false;
                    } catch (NumberFormatException e) {
                        System.out.println("Please provide us with an valid amount and an User-Id");
                        userAmount = consoleService.getScanner().nextLine();
                        sendId = consoleService.getScanner().nextLine();
                    }
                }


            Boolean stillHere = true;
            while (stillHere) {
                if (userIDtosend == currentUserId) {
                    System.out.println("Please don't use your ID");
                    try {
                        sendId = consoleService.getScanner().nextLine();
                        userIDtosend = Integer.valueOf(sendId);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please try again!");
                        sendId = consoleService.getScanner().nextLine();
                    }
                }
                stillHere = false;
                // TODO Auto-generated method stub
            }
            boolean miniLoop = true;
            boolean secondCheck = true;
            while (secondCheck) {
                while (miniLoop) {
                    BigDecimal firstCheck = accountbalance;
                    if (amountToSend.compareTo(BigDecimal.valueOf(0))==-1|| amountToSend.compareTo(firstCheck)==1) {
                        System.out.println("**** Please enter an amount that is less than your balance and greater than 0 ****");
                        try {
                            userAmount = consoleService.getScanner().nextLine();
                            amountToSend = BigDecimal.valueOf(Integer.valueOf(userAmount));
                            secondCheck = false;

                        } catch (NumberFormatException e) {
                            System.out.println("Please enter an number");
                            userAmount = consoleService.getScanner().nextLine();
                        }
                    } else {
                        break;
//                        System.out.println("hahhah");

                    }
                }

                BigDecimal finalCheck = accountbalance;
                if (amountToSend.compareTo(BigDecimal.valueOf(1))==1 && amountToSend.compareTo(finalCheck)==-1) {
                    Transfers newTransfer = new Transfers();
                    newTransfer.setAmount(amountToSend);
                    newTransfer.setUserTo(userIDtosend);
                    newTransfer.setUserFrom(currentUser.getUser().getId());
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);

                    HttpEntity<Transfers> entityTransfer = new HttpEntity<>(newTransfer,headers);
                    ResponseEntity<String> response1 = resttemplate.exchange(API_BASE_URL+"transfer",HttpMethod.POST,entityTransfer, String.class);
                    resulted += response1.getBody();


                    return resulted;
                }

            }

        }

        return null;

    }

    public String confirmedStatus(BigDecimal currentbalance, AuthenticatedUser user, RestTemplate resttemplate, String API_BASE_URL, ConsoleService consoleService){

        System.out.println("Here is you current balance: "+ currentbalance);
        boolean userTrue = true;
        int currentUserId= user.getUser().getId();
        while(userTrue) {
            ResponseEntity<Map>response = resttemplate.exchange(API_BASE_URL+"listUsers",HttpMethod.GET,makeAuthEntity(),Map.class);

            Map<Integer, String> userToTenmo = new HashMap<>();
            userToTenmo = response.getBody();
            String displayUser = "";
            for (int i = 0; i < 1; i++) {
                displayUser += userToTenmo.entrySet();
                displayUser += "\t";
                String newDisplay = displayUser.replace("=", "    ");
                String replaceAgain = newDisplay.replace(",", "\n");
                String oneLast = replaceAgain.replace("[", " ");
                String oneLastOne = oneLast.replace("]", " ");
                System.out.println("----------------------------------------------");
                System.out.println(" Users\n  Id" + "      name");
                System.out.println(oneLastOne);
                System.out.println("------\n" + "\n");
                displayUser = "";
            }
            System.out.print("Enter ID of user you are requesting fom (0) to cancel: ");
            String userId = consoleService.getScanner().nextLine();
            int firstCheck = Integer.valueOf(userId);
            if(firstCheck==0){
                return "Good-Bye";
            }
            System.out.print("Enter amount:");
            String amount = consoleService.getScanner().nextLine();
            BigDecimal amountToSend = BigDecimal.valueOf(0);
            int userIDtosend = 0;
            boolean isValidNumber = true;
            while (isValidNumber) {
                try {
                    userIDtosend = Integer.valueOf(userId);
                    BigDecimal newGuy = new BigDecimal(amount);
                    amountToSend=newGuy;
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Please provide us with a valid Amount and a UserId");
                    userId = consoleService.getScanner().nextLine();
                    amount = consoleService.getScanner().nextLine();
                }
            }

            Boolean stillHere = true;
            while (stillHere) {
                if (userIDtosend == currentUserId) {
                    System.out.println("Please do not use your ID");
                    try {
                        userId = consoleService.getScanner().nextLine();
                        userIDtosend = Integer.valueOf(userId);
                        break;
                    } catch (NumberFormatException e) {
                        System.out.println("Please try again!");
                        userId = consoleService.getScanner().nextLine();
                    }
                }

                stillHere = false;
            }
            boolean miniLoop = true;
            boolean secondCheck = true;
            while (secondCheck) {
                while (miniLoop) {
                    if (amountToSend.compareTo(BigDecimal.valueOf(0))==-1) {
                        System.out.println("Please check your amount, the given value is invalid");
                        try {
                            amount = consoleService.getScanner().nextLine();
                            BigDecimal tryAgain = new BigDecimal(amount);
                            amountToSend = tryAgain;
                            break;
                        } catch (NumberFormatException e) {
                            System.out.println("Please enter a number");
                            amount = consoleService.getScanner().nextLine();
                        }
                    }
                    break;
                }
                BigDecimal checkFinal = new BigDecimal(0.01);
                if (amountToSend.compareTo(checkFinal)==1) {
                    break;
                }
            }

            Transfers transferRequest = new Transfers();
            transferRequest.setUserFrom(user.getUser().getId());
            transferRequest.setUserTo(userIDtosend);
            transferRequest.setAmount(amountToSend);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Transfers>newTransfer = new HttpEntity<>(transferRequest,header);
            String approve = "";
            ResponseEntity<String>response1 = resttemplate.exchange(API_BASE_URL+"transfer/request",HttpMethod.POST,newTransfer,String.class);
            approve+= response1.getBody();

return approve;
        }

        return null;
    }


    public void getTransferHistory(AuthenticatedUser currentUser, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL) {
        int currentUserId = currentUser.getUser().getId();

        ResponseEntity<Transfers[]> transfersArrays = resttemplate.exchange(API_BASE_URL + "transfers/myTransfers", HttpMethod.GET  , makeAuthEntity() , Transfers[].class);
        Transfers[] transfersArray = transfersArrays.getBody();
        if (transfersArray != null && transfersArray.length > 0) {
            List<Transfers> transfers = Arrays.asList(transfersArray);
            System.out.println("-------------------------------------------");
            System.out.println("Transfers");
            System.out.println("ID          Amount                 From/To");
            System.out.println("-------------------------------------------");



            for (Transfers transfer : transfers) {
                String fromTo;
                String toUser = "";

                if (transfer.getFromUsername().contains(currentUser.getUser().getUsername())) {
                    fromTo = "From:  " + transfer.getFromUsername().substring(0,1).toUpperCase()+transfer.getFromUsername().substring(1)+"\n";
                    toUser = "To:   "+ transfer.getToUsername().substring(0,1).toUpperCase()+transfer.getToUsername().substring(1);

                } else {
                    fromTo = " To:  " + transfer.getToUsername().substring(0,1).toUpperCase()+transfer.getToUsername().substring(1)+"\n";
                    toUser = "From: "+ transfer.getFromUsername().substring(0,1).toUpperCase()+transfer.getFromUsername().substring(1);

                }

                int transferId = transfer.getTransferId();
                BigDecimal amount = transfer.getAmount();

                String transferIdStr = transferId + "";
                while (transferIdStr.length() < 12) {
                    transferIdStr = transferIdStr + " ";
                }

                while (fromTo.length() < 20) {
                    fromTo = fromTo + " ";
                }

                String amountStr = "$ " + amount.setScale(2, RoundingMode.HALF_UP);

                System.out.print(transferIdStr);
                System.out.print(amountStr+"              ");
                System.out.println(fromTo+"                             "+toUser);
                System.out.println("\n");

            }
            System.out.print("\nEnter transfer ID to view details (0 to cancel): ");
            int transferId = consoleService.promptForInt("Transfer ID: ");

            if (transferId != 0) {
                Transfers selectedTransfer = null;

                for (Transfers transfer : transfers) {
                    if (transfer.getTransferId() == transferId) {
                        selectedTransfer = transfer;
                        break;
                    }
                }

                if (selectedTransfer != null) {
                    viewTransferDetails(selectedTransfer);
                } else {
                    System.out.println("Invalid transfer ID.");
                }
            }
        }
    }

    public void viewTransferDetails(Transfers transfer) {
        System.out.println("--------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("--------------------------------------------");
        System.out.println(" Id: " + transfer.getTransferId());
        System.out.println(" From: " + transfer.getFromUsername().substring(0,1).toUpperCase()+transfer.getFromUsername().substring(1));
        System.out.println(" To: " + transfer.getToUsername().substring(0,1).toUpperCase()+transfer.getToUsername().substring(1));

        String transferType = mapTransferType(transfer.getTransferTypeId());

        String transferStatus = mapTransferStatus(transfer.getTransferStatusId());

        System.out.println(" Type: " + "Send");
        System.out.println(" Status: " + "Approved");

        // Format amount using BigDecimal
        BigDecimal amount = transfer.getAmount();
        String amountStr = "$" + amount.setScale(2, RoundingMode.HALF_UP);

        System.out.println(" Amount: " + amountStr);
    }
    public void viewPendingRequests(BigDecimal currentBalance, AuthenticatedUser userId, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL) {
        ResponseEntity<Transfers[]> transfersArrays = resttemplate.exchange(API_BASE_URL + "transfer/pending", HttpMethod.GET  , makeAuthEntity() , Transfers[].class);
        Transfers[] transfersArray = transfersArrays.getBody();

        if (transfersArray != null && transfersArray.length > 0) {
            List<Transfers> transfers = Arrays.asList(transfersArray);

            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID             To                     Amount");
            System.out.println("-------------------------------------------");

            for (Transfers transfer : transfers) {
                String fromUsername = transfer.getFromUsername().substring(0, 1).toUpperCase() + transfer.getFromUsername().substring(1);
                BigDecimal amount = transfer.getAmount();
                String amountStr = "$ " + amount.setScale(2, RoundingMode.HALF_UP);

                System.out.print(transfer.getTransferId() + "      ");
                System.out.print(fromUsername + "       ");
                System.out.println("             " + amountStr);

            }
            System.out.println("\n" + "Please enter transfer ID to approve/reject (0 to cancel)");
            String transferID = consoleService.getScanner().nextLine();
            int options = consoleService.promptForInt(transferID);
            if (options == 0) {
                consoleService.printMainMenu();
            } else if (options >= 1) {
                Transfers transferForConfirm = resttemplate.getForObject(API_BASE_URL + "transfer/request/" + options, Transfers.class);

                if (transferForConfirm != null) {
                    System.out.println("**************");
                    System.out.println("1: Approve");
                    System.out.println("2: Reject");
                    System.out.println("3: Don't approve or reject");
                    String userInput = consoleService.getScanner().nextLine();
                    int numberStatus = consoleService.promptForInt(userInput);
                    if (numberStatus == 1) {
                        if (transferForConfirm.getAmount().intValue() >= currentBalance.intValue()) {
                            System.out.println("You do not have enough money to approve this request");
                        } else {

                            String helloAgain = resttemplate.getForObject(API_BASE_URL + "requestsQ/" + transferForConfirm.getTransferId(), String.class);
                            System.out.println(helloAgain);
                        }
                    }
                    if (numberStatus == 2) {
                        String helloMyfriend = resttemplate.getForObject(API_BASE_URL + "transfers/requests/" + transferForConfirm.getTransferId(), String.class);
                        System.out.println(helloMyfriend);

                    }
                    if (numberStatus == 3) {
                        System.out.println("* Still Pending *");
                    }
                    else if (numberStatus>4 || numberStatus<0){
                        System.out.println("That is an invalid entry, Good bye");
                        consoleService.printMainMenu();
                    }

                } else {
                    System.out.println("No pending transfers found.");
                }
            }
        }
    }

    private String mapTransferType(int typeId) {
        switch (typeId) {
            case 1:
                return "Request";
            case 2:
                return "Send";
            default:
                return "Unknown";
        }
    }

    // Method to map transfer status ID to description (example)
    private String mapTransferStatus(int statusId) {
        switch (statusId) {
            case 1:
                return "Pending";
            case 2:
                return "Approved";
            case 3:
                return "Rejected";
            default:
                return "Unknown";
        }
    }

    public void lisOfRequest(BigDecimal userBalance){
        System.out.println("Here is your current balance: "+ userBalance);

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}