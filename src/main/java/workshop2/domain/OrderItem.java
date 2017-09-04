/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 *
 * @author Ahmed Al-Alaaq(Egelantier)
 */
@Entity
@Table(name = "ORDERITEM")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private int id;
    @OneToOne
    private Order order;
    @OneToOne
    private Product product;
    @Column(name = "AMOUNT")
    private int amount;
    @Column(name = "SUBTOTAL")
    private BigDecimal subTotal;

    // Default no-arg constructor will leave all member fields on their default
    // except for the id field which will be invalidated to a negative value
    public OrderItem() {

    }

    // Constructor without id, id will be invalidated to a negative value
    public OrderItem(Order order, Product product, int amount, BigDecimal subTotal) {
        this.order = order;
        this.product = product;
        this.amount = amount;
        this.subTotal = subTotal;
    }

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
        hash = 83 * hash + this.order.hashCode();
        hash = 83 * hash + this.product.hashCode();
        hash = 83 * hash + this.amount;
        hash = 83 * hash + Objects.hashCode(this.subTotal);
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
        final OrderItem other = (OrderItem) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.order != other.order) {
            return false;
        }
        if (this.product != other.product) {
            return false;
        }
        if (this.amount != other.amount) {
            return false;
        }
        if (!Objects.equals(this.subTotal, other.subTotal)) {
            return false;
        }
        return true;
    }

}