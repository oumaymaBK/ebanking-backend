package org.sid.ebankingbackend.exceptions;

public class CustomerNotFoundException extends Exception {// extends RuntimeException :RuntimeException :ne se mettre pas dans le méthode
   // mais avec exception elle se mettre dans la mèthode comme si on dit voila cette méthode peut gérer une exception
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
