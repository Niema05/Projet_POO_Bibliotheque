package com.bibliotheque.model;


public interface Empruntable {
  
    boolean peutEtreEmprunte();

    void emprunter();

    void retourner();
}

