package org.sid.ebankingbackend.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.*;
import org.sid.ebankingbackend.entities.*;
import org.sid.ebankingbackend.enums.OperationType;
import org.sid.ebankingbackend.exceptions.BankAccountNotFoundException;
import org.sid.ebankingbackend.exceptions.BanlanceNotSufficientException;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.mappers.BankAccountMapperImpl;
import org.sid.ebankingbackend.repositories.AccountOperationRepository;
import org.sid.ebankingbackend.repositories.BankAccountRepository;
import org.sid.ebankingbackend.repositories.CustomerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

//on doit implementer" l'interface de service bank account"
@Service
@Transactional
@AllArgsConstructor //avec lombok on peut faire l'injection de dependence à traver AllargsConstructor
@Slf4j //Pour le log c'est à dire loger un message(écrire un message)
public class  BankAccountServiceImpl  implements  BankAccountService {
    //@Autowired //pour faire l'injection de dépendences
    //On peut utiliser un constructeur de 3 parametres lors de @  Auowired
    // j'ai besoin d'utiliser
    private CustomerRepository customerRepository;
    //@Autowired
    private BankAccountRepository bankAccountRepository;
    //@Autowired
    private AccountOperationRepository accountOperationRepository;

    //public BankAccountServiceImpl( CustomerRepository customerRepository,BankAccountRepository bankAccountRepository,AccountOperationRepository accountOperationRepository) {
    // this.customerRepository = customerRepository;
    // this.bankAccountRepository = bankAccountRepository;
    //this.accountOperationRepository = accountOperationRepository;}
    private BankAccountMapperImpl dtoMapper;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Saving new Customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO);//on doit le transférer une autre fois en Customer
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);//on doit le transférer une autre fois en CustomerDTO
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        CurrentAccount currentAccount = new CurrentAccount();
        currentAccount.setId(UUID.randomUUID().toString());
        currentAccount.setCreatedAt(new Date());
        currentAccount.setBalance(initialBalance);
        currentAccount.setOverDraft(overDraft);
        currentAccount.setCustomer(customer);
        CurrentAccount savedBankAccount = bankAccountRepository.save(currentAccount);
        return dtoMapper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);//s il n'existe pas return null en utilisant exception
        if (customer == null)
            throw new CustomerNotFoundException("Customer not found");
        SavingAccount savingAccount = new SavingAccount();
        savingAccount.setId(UUID.randomUUID().toString());
        savingAccount.setCreatedAt(new Date());
        savingAccount.setBalance(initialBalance);
        savingAccount.setInterestRate(interestRate);
        savingAccount.setCustomer(customer);
        SavingAccount savedBankAccount = bankAccountRepository.save(savingAccount);
        return dtoMapper.fromSavingBankAccount(savedBankAccount);

    }

    @Override
    public List<CustomerDTO> listCustomers() {
        List<Customer> customers = customerRepository.findAll();
        //transfert chaque objet je veux  je genere un objet cu et je transef se stream vers list

        List<CustomerDTO> customerDTOS = customers.stream()
                .map(customer -> dtoMapper.fromCustomer(customer))
                .collect(Collectors.toList());
        return customerDTOS;
    }

    @Override
    public  BankAccountDTO getBankAccount(String accountId) throws BankAccountNotFoundException {
        BankAccount bankAccount=bankAccountRepository.findById(accountId)
                .orElseThrow(()->new BankAccountNotFoundException("BankAccount not found"));
        if(bankAccount instanceof SavingAccount){
            SavingAccount savingAccount= (SavingAccount) bankAccount;
            return dtoMapper.fromSavingBankAccount(savingAccount);
        } else {
            CurrentAccount currentAccount= (CurrentAccount) bankAccount;
            return dtoMapper.fromCurrentBankAccount(currentAccount);
        }

    }


    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundException, BanlanceNotSufficientException {
        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));
        if (bankAccount.getBalance() < amount)
            throw new BanlanceNotSufficientException("Balance not found");
        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.DEBIT);
        accountOperation.setAmount(amount);//le montant ili mich tojbdou
        accountOperation.setOperationDate(new Date());
        //cette opération touche quel compte?
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        //il suffit j'enregistre l'operation de débit le montant se change
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        bankAccountRepository.save(bankAccount);


    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundException {

        BankAccount bankAccount = bankAccountRepository.findById(accountId).
                orElseThrow(() -> new BankAccountNotFoundException("BankAccount not found"));

        AccountOperation accountOperation = new AccountOperation();
        accountOperation.setType(OperationType.CREDIT);
        accountOperation.setAmount(amount);//le montant ili mich tojbdou
        accountOperation.setOperationDate(new Date());
        //cette opération touche quel compte?
        accountOperation.setBankAccount(bankAccount);
        accountOperationRepository.save(accountOperation);
        //il suffit j'enregistre l'operation de débit le montant se change
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        bankAccountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankAccountNotFoundException, BanlanceNotSufficientException {
        debit(accountIdSource, amount, "Transfer to " + accountIdDestination);
        credit(accountIdDestination, amount, "Transfer from " + accountIdSource);


    }

    @Override
    public List<BankAccountDTO> bankAccountList() {
        List<BankAccount> bankAccounts=bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS=bankAccounts.stream().map(bankAccount ->
        {
            if(bankAccount instanceof  SavingAccount){
                SavingAccount savingAccount =(SavingAccount) bankAccount;
                return  dtoMapper.fromSavingBankAccount(savingAccount);
            }else{
                CurrentAccount currentAccount = (CurrentAccount) bankAccount;
                return dtoMapper.fromCurrentBankAccount(currentAccount);

            }
        }).collect(Collectors.toList());
        return  bankAccountDTOS;
    }

   @Override //pour ajouter la méthode à l'interface
    public CustomerDTO getCustomer(long customerId) throws CustomerNotFoundException {
       // récupération d'objet de type customer
        Customer customer =customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Customer Not found"));
    return dtoMapper.fromCustomer(customer);}



    @Override
    public CustomerDTO updateCustomer(CustomerDTO customerDTO) {
        log.info("new Customer");
        Customer customer =dtoMapper.fromCustomerDTO(customerDTO);//on doit le transférer une autre fois en Customer
        Customer savedCustomer = customerRepository.save(customer);
        return dtoMapper.fromCustomer(savedCustomer);//on doit le transférer une autre fois en CustomerDTO
    }
    @Override
    public void deleteCustomer(long customerId){
        customerRepository.deleteById(customerId);
    }

    @Override
    //Une méthode qui retourner une liste des opérations
    //pour consulter l'historique en opération de chaque compte
    public List<AccountOperationDTO> accountHistory(String accountId){
        List<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountId(accountId);
        // dans cette liste List<AccountOperation>:je transforme chaque Operation en OperationDTO + je fais collect de tous ses operationsDTO
           return  accountOperations.stream().map(op->dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundException {
        //besoin d'id de compte
        BankAccount bankAccount=bankAccountRepository.findById(accountId).orElse(null);
        if(bankAccount==null) throw new BankAccountNotFoundException("Account not Found");
        Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByOperationDateDesc(accountId,PageRequest.of(page, size));
        AccountHistoryDTO accountHistoryDTO=new AccountHistoryDTO();
        List<AccountOperationDTO> accountOperationDTOS = accountOperations.getContent().stream().map(op -> dtoMapper.fromAccountOperation(op)).collect(Collectors.toList());
       // Iniatilisation des variables d'accountHistoryDTO
        accountHistoryDTO.setAccountOperationDTOS(accountOperationDTOS);
        accountHistoryDTO.setAccountId(bankAccount.getId());
        accountHistoryDTO.setBalance(bankAccount.getBalance());
        accountHistoryDTO.setCurrentPage(page);
        accountHistoryDTO.setPageSize(size);
        accountHistoryDTO.setTotalPages(accountOperations.getTotalPages());
        return accountHistoryDTO;
    }


}




//@Override
    // public BankAccount saveBankAccount(double initialBalance, String type, Long customerId) throws CustomerNotFoundException {
       // Customer customer=customerRepository.findById(customerId).orElse(  null);
    // if(customer==null){
    //throw new CustomerNotFoundException("Customer not Found");
            //Exception
            //}
    // BankAccount bankAccount;
    //if(type.equals("current")){
    //bankAccount= new CurrentAccount();

    // }
// else {
    // bankAccount = new SavingAccount();


// bankAccount.setId(UUID.randomUUID().toString());//donne Id aléatoire
//bankAccount.setCreatedAt(new Date());
//bankAccount.setBalance(initialBalance);
// bankAccount.setCustomer(customer);
//
//return null;
//}

