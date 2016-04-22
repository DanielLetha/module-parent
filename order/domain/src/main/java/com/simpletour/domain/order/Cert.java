package com.simpletour.domain.order;

import com.simpletour.commons.data.domain.BaseDomain;

import javax.persistence.*;

/**
 * 凭证座位
 * Created by Mario on 2016/4/22.
 */
@Entity
@Table(name = "ORD_CERT")
public class Cert extends BaseDomain {

    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;


    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(Long id) {

    }
}
