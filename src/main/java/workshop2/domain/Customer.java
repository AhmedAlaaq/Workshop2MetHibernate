/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Al-Alaaq(Egelantier)
 */
@Entity
@Table(name = "CUSTOMER")
public class Customer {
     @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int id;
     @Column(name = "FIRSTNAME")
    private String firstName;
     @Column(name = "LASTNAME")
    private String lastName;
     @Column(name = "LASTNAMEPRE")
    private String lastNamePrefix;
     @OneToOne
    private Account account;
    
    public Customer(){
        
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
       
    }
    
    public Customer(String firstName, String lastName, String lastNamePrefix, Account account) {
        
        this.firstName = firstName;
        this.lastName = lastName;
        this.lastNamePrefix = lastNamePrefix;
        this.account = account;
    }
    
    public int getId() {
        return id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastNamePrefix() {
        return lastNamePrefix;
    }

    public void setLastNamePrefix(String lastNamePrefix) {
        this.lastNamePrefix = lastNamePrefix;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
    
    @Override
    public String toString(){
        if(getLastNamePrefix() != null) 
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), getLastNamePrefix(), getLastName(),
                    this.getAccount().getType());
        
        else
            return String.format("%-5d%-20s%-15s%-20s", getId(), getFirstName(), "", getLastName(),  this.getAccount().getType());
    }
    
    public String toStringNoId(){
        if(getLastNamePrefix() != null) 
            return String.format("%-5d%-20s%-15s%-20s", getFirstName(), getLastNamePrefix(), getLastName(),
                    this.getAccount().getType());
        
        else
            return String.format("%-5d%-20s%-15s%-20s", getFirstName(), "", getLastName(),  this.getAccount().getType());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.firstName);
        hash = 73 * hash + Objects.hashCode(this.lastName);
        hash = 73 * hash + Objects.hashCode(this.lastNamePrefix);
        hash = 73 * hash + this.getAccount().hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Customer other = (Customer) obj;
        if (this.id != other.id) {
            return false;
        }
        if (!Objects.equals(this.account, other.account)) {
            return false;
        }
        if (!Objects.equals(this.firstName, other.firstName)) {
            return false;
        }
        if (!Objects.equals(this.lastName, other.lastName)) {
            return false;
        }
        return Objects.equals(this.lastNamePrefix, other.lastNamePrefix);
    }
    
}
