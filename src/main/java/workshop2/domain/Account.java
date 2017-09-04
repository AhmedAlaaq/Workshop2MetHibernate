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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

/**
 *
 * @author Ahmed Al-Alaaq(Egelantier)
 */

@Entity
@Table(name = "ACCOUNT")
@SecondaryTable(name = "ACCOUNT_TYPE")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int id;
    @Column(name = "USERNAME")
    private String username;
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "TYPEID", table = "ACCOUNT_TYPE")
    private int accountTypeId;
    @Column(name = "TYPE", table = "ACCOUNT_TYPE")
    private String type;
    
    
    
    public Account(){
       
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountTypeId = accountTypeId;
    }

    public Account(String username, String password, int accountTypeId) {
        this.username = username;
        this.password = password;
        this.accountTypeId = accountTypeId;
        
    }
    
    public int getId() {
        return id;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAccountTypeId() {
        return accountTypeId;
    }

    public void setAccountType(int accountTypeId) {
        this.accountTypeId = accountTypeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
     @Override
    public String toString(){
        return String.format("%-5d%-20s%-20s%-5d", this.getId(), this.getUsername(), getPassword(), this.getAccountTypeId());
    }
    
    public String toStringNoId(){
        return String.format("%-20s%-20s%-5d", this.getUsername(), "********", this.getAccountTypeId(), this.getType());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + Objects.hashCode(this.id);
        hash = 73 * hash + Objects.hashCode(this.username);
        hash = 73 * hash + Objects.hashCode(this.password);
        hash = 73 * hash + Objects.hashCode(this.type);
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
        final Account other = (Account) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        if (!Objects.equals(this.password, other.password)) {
            return false;
        }
        return true;
    }
    
    
}
