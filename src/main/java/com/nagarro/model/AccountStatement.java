package com.nagarro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountStatement {

    private String id;
    private String accountType;
    private String accountNumber;
    private String date;
    private double amount;

}
