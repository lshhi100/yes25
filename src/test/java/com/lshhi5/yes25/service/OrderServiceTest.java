package com.lshhi5.yes25.service;

import com.lshhi5.yes25.domain.*;
import com.lshhi5.yes25.domain.item.Book;
import com.lshhi5.yes25.exception.NotEnoughStockException;
import com.lshhi5.yes25.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired private OrderService orderService;
    @Autowired private OrderRepository orderRepository;

    //주문 생성
    @Test
    public void 주문생성() {

        //given
        Member member = createMember();
        Book book = createBook("시골 jpa", 20, 10000);
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.ORDER, getOrder.getStatus(),"상품 주문시 상태는 ORDER");
        assertEquals(1,getOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야 한다.");
        assertEquals(book.getPrice()*orderCount, getOrder.getTotalPrice(), "주문한 가격은 가격*수량이다.");
        assertEquals(18, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야 한다.");

    }

    @Test
    public void 상품주문_재고수량초과() {

        //given
        Member member = createMember();
        Book book = createBook("딥워크", 10, 10000);
        int orderCount = 11;

        //when then
        assertThrows(NotEnoughStockException.class, () -> orderService.order(member.getId(), book.getId(), orderCount));
    }
    @Test
    public void 주문취소() {

        //given
        Member member = createMember();
        Book book = createBook("jpa",20,10000);
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);
        assertEquals(OrderStatus.CANCEL, getOrder.getStatus(),"주문 취소시 상태는 CANCEL 이다.");
        assertEquals(20, book.getStockQuantity(), "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    private Book createBook(String name, int stockQuantity, int price) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울","강가","123-213"));
        em.persist(member);
        return member;
    }
}