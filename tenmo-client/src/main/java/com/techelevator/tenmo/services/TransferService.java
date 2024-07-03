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



    public void getTransferHistory(AuthenticatedUser currentUser, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL) {
        int currentUserId = currentUser.getUser().getId();

        System.out.println(authToken);
        System.out.println(API_BASE_URL + "transfers/myTransfers");

        ResponseEntity<Transfers[]> transfersArrays = resttemplate.exchange(API_BASE_URL + "transfers/myTransfers", HttpMethod.GET  , makeAuthEntity() , Transfers[].class);
        Transfers[] transfersArray = transfersArrays.getBody();
        if (transfersArray != null && transfersArray.length > 0) {
            List<Transfers> transfers = Arrays.asList(transfersArray);
            System.out.println("-------------------------------------------");
            System.out.println("Transfers");
            System.out.println("ID          From/To                 Amount");
            System.out.println("-------------------------------------------");

            for (Transfers transfer : transfers) {
                String fromTo;
                if (transfer.getAccountTo() == currentUserId) {
                    fromTo = "To:    " + transfer.getToUsername();
                } else {
                    fromTo = "From:  " + transfer.getFromUsername();
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

                System.out.println(transferIdStr + fromTo + amountStr);
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
        System.out.println(" From: " + transfer.getFromUsername());
        System.out.println(" To: " + transfer.getToUsername());

        String transferType = mapTransferType(transfer.getTransferTypeId());

        String transferStatus = mapTransferStatus(transfer.getTransferStatusId());

        System.out.println(" Type: " + transferType);
        System.out.println(" Status: " + transferStatus);

        // Format amount using BigDecimal
        BigDecimal amount = transfer.getAmount();
        String amountStr = "$" + amount.setScale(2, RoundingMode.HALF_UP);

        System.out.println(" Amount: " + amountStr);
    }
    public void viewPendingRequests(AuthenticatedUser userId, RestTemplate resttemplate, ConsoleService consoleService, String API_BASE_URL) {
        ResponseEntity<Transfers[]> transfersArrays = resttemplate.exchange(API_BASE_URL + "transfers/myTransfers", HttpMethod.GET  , makeAuthEntity() , Transfers[].class);
        Transfers[] transfersArray = transfersArrays.getBody();

        if (transfersArray != null && transfersArray.length > 0) {
            List<Transfers> transfers = Arrays.asList(transfersArray);

            System.out.println("-------------------------------------------");
            System.out.println("Pending Transfers");
            System.out.println("ID          To                     Amount");
            System.out.println("-------------------------------------------");

            for (Transfers transfer : transfers) {
                String fromUsername = transfer.getFromUsername();
                BigDecimal amount = transfer.getAmount();
                String amountStr = "$ " + amount.setScale(2, RoundingMode.HALF_UP);

                System.out.printf(transfer.getTransferId() + "          " + fromUsername + "                    " + amountStr);
            }
        } else {
            System.out.println("No pending transfers found.");
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
        System.out.println("Here you current balance: "+ userBalance);

    }

    private HttpEntity<Void> makeAuthEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        return new HttpEntity<>(headers);
    }
}