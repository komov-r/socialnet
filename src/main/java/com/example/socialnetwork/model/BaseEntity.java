package com.example.socialnetwork.model;

import lombok.Getter;

/**
 * @author RKomov
 */
@Getter
public class BaseEntity {

    private Long id;


    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this.id = id;
    }
}
