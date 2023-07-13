package com.nagarro.controller;

import com.nagarro.exception.InvalidRequestParameterException;
import com.nagarro.model.AccountStatement;
import com.nagarro.model.SearchRequest;
import com.nagarro.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class StatementController {

    private final AccountService accountService;

    @PostMapping("/fetchStatement")
    public List<AccountStatement> fetchStatement(@RequestBody SearchRequest request) throws InvalidRequestParameterException, ParseException {
        return accountService.fetchStatement(request);
    }
}