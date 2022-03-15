package com.lshhi5.yes25.domain.item;

import com.lshhi5.yes25.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Entity
public abstract class Item {

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private Long stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories;
}