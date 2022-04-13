package com.adobe.bookstore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "orders")
@JsonSerialize
public class Order {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @PrePersist
    private void ensureId(){
        this.setId(UUID.randomUUID().toString());
    }


    @OneToMany(mappedBy = "order")
    Set<OrderBooks> booksSet;
}
