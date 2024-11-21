package org.sid.ebankingbackend.dtos;

import lombok.Data;

import java.util.List;
@Data
public class AccountHistoryDTO {
    private String accountId;
    private double balance;
    private int currentPage;
    //j'ai une centaine des pages d'opérations
    private int totalPages;
    private int pageSize;
    private List<AccountOperationDTO> accountOperationDTOS;
}
