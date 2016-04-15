package com.simpletour.domain.product;

/**
 * Created by yangdongfeng@simpletour.com on 2016/3/24.
 */
@Entity
@Table(name = "PROD_TOURISM")
public class Tourism {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
}
