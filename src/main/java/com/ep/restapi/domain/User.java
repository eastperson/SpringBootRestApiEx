package com.ep.restapi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder @Entity @Getter
@NoArgsConstructor @AllArgsConstructor @Table(name = "user")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long msrl;

    @Column(nullable = false,unique = true,length = 30)
    private String uid;

    @Column(nullable = false,length = 100)
    private String name;

    public User(String uid,String name) {
        this.uid = uid;
        this.name = name;
    }

}
