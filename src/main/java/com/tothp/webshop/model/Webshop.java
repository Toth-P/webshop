package com.tothp.webshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;


@Entity
@Table(name = "webshops")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Webshop {

    @Id
    @Column(name = "webshop_id")
    private String webshopId;

    @OneToMany(mappedBy = "webshop", cascade = CascadeType.ALL)
    private List<Customer> customers;

}