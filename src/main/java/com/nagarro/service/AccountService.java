package com.nagarro.service;

import com.nagarro.exception.InvalidRequestParameterException;
import com.nagarro.model.AccountStatement;
import com.nagarro.model.SearchRequest;

import java.text.ParseException;
import java.util.List;

public interface AccountService {

    List<AccountStatement> fetchStatement(SearchRequest request) throws InvalidRequestParameterException, ParseException;
}
