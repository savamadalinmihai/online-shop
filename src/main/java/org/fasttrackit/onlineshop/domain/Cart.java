package org.fasttrackit.onlineshop.domain;

import javax.persistence.*;

@Entity
public class Cart {

    @Id
    private long id;

    // this is the most efficient mapping of customer cu cart/or any other two objects
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    private Customer customer;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
