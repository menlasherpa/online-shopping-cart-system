package com.example.bank.Bank_Service.controller;

import com.example.bank.Bank_Service.rest.request.RequestedCard;
import com.example.bank.Bank_Service.rest.request.TransactionRequest;
import com.example.bank.Bank_Service.rest.response.TransactionResponse;
import com.example.bank.Bank_Service.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/banking")
public class BalanceController {




    @Autowired
    private final BankService bankService;

    @PostMapping
    public TransactionResponse checkCard(@RequestBody TransactionRequest transactionRequest) {
        RequestedCard request;
            request =  bankService.getCardDetails(transactionRequest.getCardDetails().getCardNumber());

            if(request.getCardNumber().equals("0")){
                return TransactionResponse.builder().message("No card found with that number").build();
            }

            if(transactionRequest.getCardDetails().getCvv().toString().length() != 3){
                return TransactionResponse.builder().message("CVV must be 3 digits").build();

            }

        if(!transactionRequest.getCardDetails().getCvv().toString().equals(request.getCvv())){
            return TransactionResponse.builder().message("CVV Digits are not correct").build();

        }

            if(!transactionRequest.getCardDetails().getOperationMode().equals("Active")){
                return TransactionResponse.builder().message("Card is not active").build();
            }

        if(!transactionRequest.getCardDetails().getNameOnCard().equals(request.getNameOnCard())){
            return TransactionResponse.builder().message("Name on Card is not correct").build();
        }

            TransactionResponse transactionResponse = bankService.checkCard(request , transactionRequest);


            return TransactionResponse.builder().transactionStatus(transactionResponse.getTransactionStatus()).transactionId(transactionResponse.getTransactionId()).message("OK").build();


    }
    @GetMapping("/visa")
    public ResponseEntity getVisaBalances(Pageable pageable) {
        return ResponseEntity.ok(bankService.readVisaBalances(pageable));
    }
    @GetMapping("master")
    public ResponseEntity getMasterBalances(Pageable pageable) {
        return ResponseEntity.ok(bankService.readMasterBalances(pageable));
    }


}