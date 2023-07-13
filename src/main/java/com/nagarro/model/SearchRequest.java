package com.nagarro.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    private String accountId;
    private String fromDate;
    private String toDate;
    private String fromAmount;
    private String toAmount;

}
