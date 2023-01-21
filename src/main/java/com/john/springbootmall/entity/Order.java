package com.john.springbootmall.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Integer orderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Column(name = "created_date", updatable = false)
    private Date createdDate;

    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    @Transient
    private List<OrderItem> orderItemList;
}
