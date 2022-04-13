package com.adobe.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

@EnableAsync
@Service
public class OrderService {

    BookStockRepository bookStockRepository;

    OrderRepository orderRepository;

    OrderBooksRepository orderBooksRepository;

    @Autowired
    public OrderService(BookStockRepository bookStockRepository, OrderRepository orderRepository, OrderBooksRepository orderBooksRepository) {
        this.bookStockRepository = bookStockRepository;
        this.orderBooksRepository = orderBooksRepository;
        this.orderRepository = orderRepository;
    }


    @Async
    public void asyncProcessOrder(Order order, OrderBookWrapper orderBookWrapper) {
        for (SingleOrderBookWrapper book: orderBookWrapper.getBooks()) {
            BookStock bookStock = bookStockRepository.getById(book.getBook());
            OrderBooks orderBook = new OrderBooks();
            orderBook.setOrder(order);
            orderBook.setBook(bookStock);
            orderBook.setQuantity(book.getQuantity());
            orderBooksRepository.save(orderBook);

            bookStock.setQuantity(bookStock.getQuantity() - book.getQuantity());
            bookStockRepository.save(bookStock);
        }

    }
}
