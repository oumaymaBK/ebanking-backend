package org.sid.ebankingbackend.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("CA")//C-à-dire en créant le currentaccount sa valeur sera enregistré sous forme d'une variable CA dans la colonne TYPE de bankaccount
@Data
@NoArgsConstructor
@AllArgsConstructor
//chaque currentAccount est un account ==>nécisste extends de BankAccount
public class CurrentAccount extends  BankAccount{
    //il a une découverte
    private double overDraft;
}
