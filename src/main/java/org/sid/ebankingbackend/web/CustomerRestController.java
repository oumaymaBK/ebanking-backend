package org.sid.ebankingbackend.web;
//on va faire un contolleur pour gérer les clients


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sid.ebankingbackend.dtos.CustomerDTO;
import org.sid.ebankingbackend.entities.Customer;
import org.sid.ebankingbackend.exceptions.CustomerNotFoundException;
import org.sid.ebankingbackend.services.BankAccountService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RequestMapping
@RestController
@AllArgsConstructor //pour faire l'injection de dépendence
@Slf4j //pour utiliser la log
public class CustomerRestController {
    //j'ai besoin d'utiliser la couche service
    private BankAccountService bankAccountService;
    // Consultation de liste de clients
    //envoyer la requéte get pour avoir la liste

   // Méthode:consultation list  des client
    @GetMapping("/customers")
    public List<CustomerDTO> customers(){
        return bankAccountService.listCustomers();

    }
    // Méthode:consultation de client par id
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer (@PathVariable(name ="id") long customerId) throws CustomerNotFoundException {
           return  bankAccountService.getCustomer(customerId);
    }
    //Méthode de Création de customer
    @PostMapping("/customers")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        //On va réquépirer les données sous formes de json à partir
        //de corps de la réquéte
        return bankAccountService.saveCustomer(customerDTO);

    }
    @PutMapping("/customers/{customerId}")
    public CustomerDTO updateCustomer(@PathVariable long customerId, @RequestBody  CustomerDTO customerDTO ){
        // @restrequest les données de customer provient d'une requete
        customerDTO.setId(customerId);//id de path non id de classe

        return bankAccountService.updateCustomer(customerDTO);

    }
@DeleteMapping("/customers/{id}")
    public void  deleteCustomer( @PathVariable  Long id){
        bankAccountService.deleteCustomer(id);
    }


}
