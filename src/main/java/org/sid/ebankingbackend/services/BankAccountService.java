package org.sid.ebankingbackend.services;

import jakarta.persistence.Id;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.BankAccount;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.BanlanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customer);//output et input CustomerDTO
    //j'ai besoin de créer un compte
    //j'ai besoin de spécifier le solde initial + type de compte(current ou saving) et le client
    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException;
    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException;
    // consuler la list des clients
    List<CustomerDTO> listCustomers();
   // Consuler aussi le compte
    BankAccountDTO getBankAccount(String accounId) throws BankAccountNotFoundException;
    //l'opération de débit
    void debit(String accounId, double amount, String description) throws BankAccountNotFoundException, BanlanceNotSufficientException;
    void credit(String accounId, double amount, String description) throws BankAccountNotFoundException;
    // l'peration de transfert nécessite deux account
    void transfer(String accounIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BanlanceNotSufficientException;


    List<BankAccountDTO> bankAccountList();

    CustomerDTO getCustomer(long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO);

    void deleteCustomer(long customerId);

    //Une méthode qui retourner une liste des opérations
    //pour consulter l'historique en opération de chaque compte
    List<AccountOperationDTO> accountHistory(String accountId);


    AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException;
}
