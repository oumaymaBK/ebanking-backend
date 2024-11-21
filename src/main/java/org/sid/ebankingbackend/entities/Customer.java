package org.sid.ebankingbackend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//on va faire le mapping objet relatioennelle1
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    //on va faire le mapping objet relatioennelle 2
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //Attributs customer
    private Long id;
    private String name;
    private String email;
    //un client peut avoir plusieurs comptes+lorsque on utilise la relation OnetoMany le BankingAccount sera comme entity
    @OneToMany(mappedBy = "customer")//3ibara 9a3da n9olou rahou fama relation identitique liha  rahi deja utilisée déja mappée (relation bidirectionnelle)fil liste ili 7atitha
    //mta3 BankCount tista3mil fil attribut customer
    //un client peut avoir plusieurs comptes
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)//ignorer pour la lecture:write.only
    //quant j'ai consulté un client j'ai n'ai pas besoin de consulter tous les comptes et a chaque compte j'ai besoin de consulter un client
    //boucle infini
    //un defaut de relation bidirectionnelle
    private List<BankAccount> bankAccounts;
}
