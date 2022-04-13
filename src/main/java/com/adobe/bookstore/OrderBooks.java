package com.adobe.bookstore;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import javax.persistence.*;
import java.awt.print.Book;
import java.io.Serializable;

@Embeddable
class OrderBookKey implements Serializable {
    @Column(name = "order_id", nullable = false)
    private String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Column(name = "book_id", nullable = false)
    private String bookId;

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

}


@Entity
@Table(name = "order_books")
@JsonSerialize
public class OrderBooks {

    @EmbeddedId
    public OrderBookKey id;

    public void setBook(BookStock book) {
        this.book = book;
        this.id.setBookId(book.getId());
    }
    public void setOrder(Order order) {
        this.id.setOrderId(order.getId());
        this.order = order;
    }
    public String getBookId() {
        return this.id.getBookId();
    }
    public String getOrderId() {
        return this.id.getOrderId();
    }


    public OrderBooks() {
        this.id = new OrderBookKey();
        this.order = new Order();
        this.book = new BookStock();
    }

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    BookStock book;


    @Column(name = "quantity", nullable = false)
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int qty) {
        this.quantity = qty;
    }


}
