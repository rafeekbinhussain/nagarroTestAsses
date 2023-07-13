package com.nagarro.service.impl;

import com.nagarro.exception.InvalidRequestParameterException;
import com.nagarro.model.AccountStatement;
import com.nagarro.model.SearchRequest;
import com.nagarro.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final JdbcTemplate jdbcTemplate;
    private static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    public List<AccountStatement> fetchStatement(SearchRequest request) throws InvalidRequestParameterException, ParseException {
        log.info("Fetching account statement for account: {}", request.getAccountId());
        log.info("Checking user is admin or not");
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .anyMatch(role -> role.toString().equalsIgnoreCase(ROLE_ADMIN));
        log.info("is Admin: {}", isAdmin);
        if (!isAdmin) {
            if (!request.getFromDate().isEmpty() || !request.getToDate().isEmpty() || !request.getFromAmount().isEmpty() || !request.getToAmount().isEmpty())
                throw new InvalidRequestParameterException("You don't have access for this resource");
            String query = String.format("SELECT account_id, datefield, amount from account where account_id = %s", getAccountId(request.getAccountId()));
            log.info("Trying to execute query: {}", query);
            return fetchStatement(query);
        } else {
            String query = String.format("SELECT account_id, datefield, amount from account where account_id = %s", getAccountId(request.getAccountId()));
            log.info("Checking From and To date: {}, {}", request.getFromDate(), request.getToDate());
            if (!request.getFromDate().isEmpty()) {
                if (request.getToDate().isEmpty()) request.setToDate("default_today_date");
                SimpleDateFormat format = new SimpleDateFormat("");
                Date fromDate = format.parse(request.getFromDate());
                Date toDate = format.parse(request.getToDate());
                if (fromDate.after(toDate))
                    throw new InvalidRequestParameterException("From Date cannot be greater then or equal to current date");
                query += String.format("and datefield >= %s and datefield <= %s", request.getFromDate(), request.getToDate());
            } else if (!request.getFromAmount().isEmpty() && !request.getToAmount().isEmpty()) {
                log.info("checking amount: {}, {}", request.getFromAmount(), request.getToAmount());
                double fromAmount = parseAmount(request.getFromAmount());
                double toAmount = parseAmount(request.getToAmount());
                query += String.format("and amount >= %s and amount <= %s", fromAmount, toAmount);
            }
            log.info("Trying to execute query: {}", query);
            return fetchStatement(query);
        }
    }

    private double parseAmount(String amount) {
        try {
            double parsedAmount = Double.parseDouble(amount);
            log.info("parse amount: {}", parsedAmount);
            return parsedAmount;
        } catch (NumberFormatException | NullPointerException nfe) {
            log.error("Invalid Amount", nfe);
            throw new IllegalArgumentException("Invalid Amount Passed");
        }
    }
    
    private String getAccountId(String accountNumber){
        return jdbcTemplate.query("SELECT account_id from account where account_number = "+ accountNumber, (ResultSet result) -> result.getString(1));
    }

    private List<AccountStatement> fetchStatement(String query){
        List<AccountStatement> statement = jdbcTemplate.query(query, (rs, rowNum) -> AccountStatement.builder()
                .id(rs.getString(1))
                .accountNumber(rs.getString(2))
                .accountType(rs.getString(3))
                .amount(rs.getDouble(4))
                .date(rs.getString(5))
                .build());
        log.info("Found {} result", statement.size());
        return statement;
    }
}