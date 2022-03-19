package com.lshhi5.yes25.domain;

import com.lshhi5.yes25.domain.Order;
import com.lshhi5.yes25.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Getter
@Setter
//@Table(name = "order_item")
@Entity
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name="order_item_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice; //주문 가격
    private int count; //주문 수량


    //==생성 메소드==//

    //==비즈니스 로직==//
    public void cancel() {
        getItem().addStock(count);
    }

    //==조회 로직==//

    /**
     * 주문 상품 전체 가격 조회
     */
}
