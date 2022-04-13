package com.adobe.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.jdbc.Sql;


import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OrderResourceTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('12345-67890', 'some book', 7), ('111222333', 'Parry Hotter', 4)")
    public void shouldReturn200IfInStock() {
        SingleOrderBookWrapper bookWrapper1 = new SingleOrderBookWrapper();
        bookWrapper1.setBook("12345-67890");
        bookWrapper1.setQuantity(3);
        SingleOrderBookWrapper bookWrapper2 = new SingleOrderBookWrapper();
        bookWrapper2.setBook("111222333");
        bookWrapper2.setQuantity(4);
        ArrayList<SingleOrderBookWrapper> books = new ArrayList<>();
        books.add(bookWrapper1);
        books.add(bookWrapper2);
        OrderBookWrapper wrapper = new OrderBookWrapper();
        wrapper.setBooks(books);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<OrderBookWrapper> request = new HttpEntity<>(wrapper, headers);

        var result = restTemplate.postForEntity("http://localhost:" + port + "/orders", request, String.class);

        assertThat(result.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('987654321', 'another book', 6)")
    public void shouldReturn400IfOutOfStock() {
        SingleOrderBookWrapper bookWrapper = new SingleOrderBookWrapper();
        bookWrapper.setBook("987654321");
        bookWrapper.setQuantity(8);
        ArrayList<SingleOrderBookWrapper> books = new ArrayList<>();
        books.add(bookWrapper);
        OrderBookWrapper wrapper = new OrderBookWrapper();
        wrapper.setBooks(books);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<OrderBookWrapper> request = new HttpEntity<>(wrapper, headers);

        var result = restTemplate.postForEntity("http://localhost:" + port + "/orders", request, String.class);

        assertThat(result.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    @Sql(statements = "INSERT INTO book_stock (id, name, quantity) VALUES ('aaaaaaaaaa', 'third book', 7)")
    public void shouldUpdateStocks() {
        SingleOrderBookWrapper bookWrapper = new SingleOrderBookWrapper();
        bookWrapper.setBook("aaaaaaaaaa");
        bookWrapper.setQuantity(3);
        ArrayList<SingleOrderBookWrapper> books = new ArrayList<>();
        books.add(bookWrapper);
        OrderBookWrapper wrapper = new OrderBookWrapper();
        wrapper.setBooks(books);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-COM-PERSIST", "true");

        HttpEntity<OrderBookWrapper> request = new HttpEntity<>(wrapper, headers);

        var post = restTemplate.postForEntity("http://localhost:" + port + "/orders", request, String.class);


        int result = restTemplate.getForObject("http://localhost:" + port + "/books_stock/aaaaaaaaaa", BookStock.class).getQuantity();

        assertThat(result).isEqualTo(4);
    }
}
