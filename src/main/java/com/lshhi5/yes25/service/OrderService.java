package com.lshhi5.yes25.service;

import com.lshhi5.yes25.domain.Delivery;
import com.lshhi5.yes25.domain.Member;
import com.lshhi5.yes25.domain.Order;
import com.lshhi5.yes25.domain.OrderItem;
import com.lshhi5.yes25.domain.item.Item;
import com.lshhi5.yes25.repository.ItemRepository;
import com.lshhi5.yes25.repository.MemberRepository;
import com.lshhi5.yes25.repository.OrderRepository;
import com.lshhi5.yes25.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * 주문 취소
     */

    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();

    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByCriteria(orderSearch);
    }

    //검색
    public Order findOne(Long orderId) {
        return orderRepository.findOne(orderId);
    }


}
