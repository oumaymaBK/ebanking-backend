package org.sid.ebankingbackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sid.ebankingbackend.enums.OperationType;

import java.util.Date;

@Entity
// un constructeur sans paramètre: NoArgsConstructor + un constructeur avec paramétre: AllArgsConstructor

@Data
@NoArgsConstructor //lors authowrid -->injeection de dépendence
@AllArgsConstructor
public class AccountOperation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //numero operation
    private long id;
    private Date operationDate;
    private double amount;
    //type d'opération
    @Enumerated(EnumType.STRING)
    private OperationType type;
    //une opération concerne un compte
    @ManyToOne
    private BankAccount bankAccount;
    private String Description;

}
