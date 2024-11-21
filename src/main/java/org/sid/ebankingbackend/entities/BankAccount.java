package org.sid.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.AccountStatus;

import java.util.Date;
import java.util.List;

@Entity
//On a un héritage de classe account
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
//@Inheritance(strategy = InheritanceType.JOINED)//création des tables pour les classes dérivés
//on va précisier la colonne de table
@DiscriminatorColumn(name = "TYPE",length= 4)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {
    // abstract il va créer que les classes concrètes (dirivées)
    @Id
    //Attributs du compte_client
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)//pour que les données created activated suspended soient en string non en valeur
    private AccountStatus status;
    //On peut avoir plusieurs compte qui concernent un client
    @ManyToOne
    //un compte appartient à un client donc on va déclarer un objet client
    private Customer customer;
    @OneToMany(mappedBy = "bankAccount",fetch = FetchType.LAZY)//quand je vous dis charger moi le compte vous dois charger automatiquement tous les opérations de compte - mémoire saturante==>pour cela Lazy mieux charger seulement le compte et le chargement d'opération on va le faire à la demande
    private List<AccountOperation> accountOperations;
}
