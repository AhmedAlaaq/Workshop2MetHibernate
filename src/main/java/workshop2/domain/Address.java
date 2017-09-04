/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.lang.ProcessBuilder.Redirect.Type;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

/**
 *
 * @author Ahmed Al-alaaq(Egelantier)
 */
@Entity
@Table(name = "ADDRESS")
/*@SecondaryTable(name = "ADDRESS_TYPE")*/
public class Address {

    public Address(int id, String newStreetName, Integer newNumber, String newAddition, String newPostalCode, String newCity, Customer customer, Type addressType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public enum Type {
        HUIS, WERK
    }
            
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "ID")
    private int id;
    @Column(name = "STREETNAME")
    private String streetName;
    @Column(name = "NUMBER")
    private int number;
    @Column(name = "ADDITION")
    private String addition;
    @Column(name = "POSTALCODE")
    private String postalCode;
    @Column(name = "CITY")
    private String city;
    @OneToOne
    private Customer customer;
   /* @Column(name = "ADDRESSTYPEID", table = "ADDRESS_TYPE")
    private int addressTypeId;
    @Column(name = "TYPE", table = "ADDRESS_TYPE")*/
    @Enumerated(EnumType.ORDINAL)
    private Type addressType;
    
    public Address(){
        
    }

    public Address(String streetName, int number, String addition, String postalCode, 
            String city, Customer customer, Type addressType/*int addressTypeId*/) {
        this.streetName = streetName;
        this.number = number;
        this.addition = addition;
        this.postalCode = postalCode;
        this.city = city;
        this.customer = customer;
        this.addressType = addressType;
    }


    public int getId() {
        return id;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    public Type getAddressType() {
        return addressType;
    }


    public void setType(Type addressType) {
        this.addressType = addressType;
    }
    
    
    @Override
    public String toString(){
        return String.format("%-5d%-30s%-8d%-12s%-10s%-20s%-8d", getId(), getStreetName(), 
                getNumber(), getAddition(), getPostalCode(), getCity(), this.getAddressType(), this.getCustomer().getFirstName(), " ", this.getCustomer().getLastName());
    }
    
 
    public String toStringNoId(){
        return String.format("%-5d%-30s%-8d%-12s%-10s%-20s%-8d", getStreetName(), 
                getNumber(), getAddition(), getPostalCode(), getCity(), this.getAddressType(), this.getCustomer().getFirstName(), " ", this.getCustomer().getLastName());
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 73 * hash + this.id;
        hash = 73 * hash + Objects.hashCode(this.streetName);
        hash = 73 * hash + this.number;
        hash = 73 * hash + Objects.hashCode(this.addition);
        hash = 73 * hash + Objects.hashCode(this.postalCode);
        hash = 73 * hash + Objects.hashCode(this.city);
        hash = 73 * hash + Objects.hashCode(this.addressType);
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
        final Address other = (Address) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.addressType != other.addressType) {
            return false;
        }
        if (this.number != other.number) {
            return false;
        }
        if (!Objects.equals(this.streetName, other.streetName)) {
            return false;
        }
        if (!Objects.equals(this.addition, other.addition)) {
            return false;
        }
        if (!Objects.equals(this.postalCode, other.postalCode)) {
            return false;
        }
        if (!Objects.equals(this.city, other.city)) {
            return false;
        }
        
        return true;
    }
    
}