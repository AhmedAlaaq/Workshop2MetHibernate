/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

/**
 *
 * @author Ahmed Al-Alaaq(Egelantier)
 */
@Entity
@Table(name = "ORDER")
@SecondaryTable(name = "ORDER_STATUS")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @Column(name = "TOTALPRICE")
    private BigDecimal totalPrice;
    @OneToOne
    private Customer customer;
    @Column(name = "DATETIME")
    private LocalDateTime dateTime;
    @Column(name = "ORDERSTATUSID", table = "ORDER_STATUS")
    private int orderStatusId;
    @Column(name = "STATUS", table = "ORDER_STATUS")
    private String status;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public Order() {

    }

    // Constructor without id, id will be invalidated to a negative value
    public Order(BigDecimal totalPrice, Customer customer, LocalDateTime dateTime, String status) {
        this.totalPrice = totalPrice;
        this.customer = customer;
        this.dateTime = dateTime;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public LocalDateTime getDate() {
        return dateTime;
    }

    public void setDate(LocalDateTime date) {
        this.dateTime = date;
    }

    public int getOrderStatusId() {
        return orderStatusId;
    }

    public String getOrderStatusIdWord() {
        String orderStatusWord = "";
        switch (orderStatusId) {
            case 1: {
                orderStatusWord = "nieuw";
                break;
            }
            case 2: {
                orderStatusWord = "in behandeling";
                break;
            }
            case 3: {
                orderStatusWord = "afgehandeld";
                break;
            }
        }
        return orderStatusWord;
    }

    public void setOrderStatusId(int orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    @Override
    public String toString() {
        return String.format("%-5d%-10.2f%-15s%-20s", this.getId(), this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatusIdWord(),
                this.getCustomer().getFirstName(), " ", this.getCustomer().getLastName());
    }

    public String toStringNoId() {
        return String.format("%-5d%-10.2f%-15s%-20s", this.getTotalPrice(), this.getDate().toLocalDate().toString(), this.getOrderStatusIdWord(),
                this.getCustomer().getFirstName(), " ", this.getCustomer().getLastName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + this.id;
        hash = 53 * hash + Objects.hashCode(this.totalPrice);
        hash = 53 * hash + Objects.hashCode(this.customer.hashCode());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getYear());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getMonthValue());
        hash = 53 * hash + Objects.hashCode(this.dateTime.getDayOfMonth());
        hash = 53 * hash + this.orderStatusId;
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
        final Order other = (Order) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.customer != other.customer) {
            return false;
        }
        if (this.orderStatusId != other.orderStatusId) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }

    public boolean equalsNoId(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Order other = (Order) obj;
        if (this.customer != other.customer) {
            return false;
        }
        if (this.orderStatusId != other.orderStatusId) {
            return false;
        }
        if (!Objects.equals(this.totalPrice, other.totalPrice)) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getYear(), other.dateTime.getYear())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getMonthValue(), other.dateTime.getMonthValue())) {
            return false;
        }
        if (!Objects.equals(this.dateTime.getDayOfMonth(), other.dateTime.getDayOfMonth())) {
            return false;
        }
        return true;
    }

}
