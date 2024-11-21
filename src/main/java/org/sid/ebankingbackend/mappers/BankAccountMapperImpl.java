package org.sid.ebankingbackend.mappers;

import org.sid.ebankingbackend.dtos.AccountOperationDTO;
import org.sid.ebankingbackend.dtos.CurrentBankAccountDTO;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.dtos.SavingBankAccountDTO;
import org.sid.ebankingbackend.entities.AccountOperation;
import org.sid.ebankingbackend.entities.CurrentAccount;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.entities.SavingAccount;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class BankAccountMapperImpl {
    //un méthode qui retourner un customerdto
    //cette méthode a comme un input un objet customer va rendre un  objet customerDTO
    public CustomerDTO fromCustomer(Customer customer) {
        CustomerDTO customerDTO=new CustomerDTO();
        //*customerDTO.setId(customer.getId());
        //pour n'est pas besoin d'écr
        //il prend tous les attributs et le transfert dynamiquement n'est pas statique
        BeanUtils.copyProperties(customer,customerDTO);//transfer les données et combiner entre le set et le get voir*
        return  customerDTO;//JE DONNE UN CUSTOMER IL MA DONNE CUSTOMER DTO
    }


    public Customer fromCustomerDTO(CustomerDTO customerDTO){
        Customer customer=new Customer();
        BeanUtils.copyProperties(customerDTO,customer);
        return  customer;
    }

    public SavingBankAccountDTO fromSavingBankAccount(SavingAccount savingAccount){
       SavingBankAccountDTO savingBankAccountDTO = new SavingBankAccountDTO();
        BeanUtils.copyProperties(savingAccount,savingBankAccountDTO);
        savingBankAccountDTO.setCustomerDTO(fromCustomer(savingAccount.getCustomer()));
        savingBankAccountDTO.setType(savingAccount.getClass().getSimpleName());//recepére le nom de la classe objet
        return  savingBankAccountDTO;
    }
    public SavingAccount fromSavingBankAccountDTO (SavingBankAccountDTO savingBankAccountDTO){
SavingAccount savingAccount= new SavingAccount();
BeanUtils.copyProperties(savingBankAccountDTO,savingAccount);
savingAccount.setCustomer(fromCustomerDTO(savingBankAccountDTO.getCustomerDTO()));
return savingAccount;
    }

    public CurrentBankAccountDTO fromCurrentBankAccount(CurrentAccount currentAccount){
        CurrentBankAccountDTO currentBankAccountDTO=new CurrentBankAccountDTO();
        BeanUtils.copyProperties(currentAccount,currentBankAccountDTO);
        currentBankAccountDTO.setCustomerDTO(fromCustomer(currentAccount.getCustomer()));
        currentBankAccountDTO.setType(currentAccount.getClass().getSimpleName());//recepére le nom de la classe objet
        return currentBankAccountDTO;
    }
    public CurrentAccount fromCurrentBankAccountDTO (CurrentBankAccountDTO currentBankAccountDTO){
        CurrentAccount currentAccount=new CurrentAccount();
        BeanUtils.copyProperties(currentBankAccountDTO,currentAccount);
        currentAccount.setCustomer(fromCustomerDTO(currentBankAccountDTO.getCustomerDTO()));
        return currentAccount;
    }
    public AccountOperationDTO fromAccountOperation(AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties(accountOperation,accountOperationDTO);//source :accountOperation+// destination: accountOperationDTO
        return  accountOperationDTO;
    }

}