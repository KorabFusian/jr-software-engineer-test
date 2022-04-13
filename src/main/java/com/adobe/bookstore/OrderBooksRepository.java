package com.adobe.bookstore;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderBooksRepository extends JpaRepository<OrderBooks, OrderBookKey> {
}
