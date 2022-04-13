package com.adobe.bookstore;

import java.util.List;

class OrderBookWrapper {
    private List<SingleOrderBookWrapper> books;

    public List<SingleOrderBookWrapper> getBooks() {
        return books;
    }

    public void setBooks(List<SingleOrderBookWrapper> books) {
        this.books = books;
    }
}

class SingleOrderBookWrapper {
    private String book;

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
