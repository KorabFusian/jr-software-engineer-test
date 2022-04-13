package com.adobe.bookstore;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/orders")
@EnableAsync
public class OrderResource {


    private OrderRepository orderRepository;
    private BookStockRepository bookStockRepository;
    private OrderBooksRepository orderBooksRepository;

    @Autowired
    public OrderResource(OrderRepository orderRepository, BookStockRepository bookStockRepository, OrderBooksRepository orderBooksRepository) {
        this.orderRepository = orderRepository;
        this.bookStockRepository = bookStockRepository;
        this.orderBooksRepository = orderBooksRepository;
    }

    @GetMapping
    public ResponseEntity<List<Order>> getOrders() {
        // Assuming we only want the orders here, so only IDs.
        // There is no way to fit book information into the orders table (since it's a many-to-many relationship),
        // which is why that information is stored separately in the order_books table.
        // Possible improvement : adding a class that contains the relevant information from orders and order_books and
        // relay that information here instead.
        List<Order> orders = orderRepository.findAll();
        return ResponseEntity.ok(orders);
    }

    @PostMapping()
    public ResponseEntity<Order> postOrder(@RequestBody OrderBookWrapper orderBookWrapper) {

        boolean valid = true;
        for (SingleOrderBookWrapper bookWrapper : orderBookWrapper.getBooks()) {
            BookStock bookStock = bookStockRepository.getById(bookWrapper.getBook());
            if(bookStock.getQuantity() < bookWrapper.getQuantity()){
                valid = false;
                break;
            }
        }

        //only if all of the books have enough stock
        if(valid) {
            Order order = new Order();
            orderRepository.save(order);
            OrderService orderService = new OrderService(bookStockRepository, orderRepository, orderBooksRepository);
            orderService.asyncProcessOrder(order,orderBookWrapper);
            return ResponseEntity.ok(order);
        }

        // If not, we send a 400 response.
        return ResponseEntity.badRequest().build();
    }
}
