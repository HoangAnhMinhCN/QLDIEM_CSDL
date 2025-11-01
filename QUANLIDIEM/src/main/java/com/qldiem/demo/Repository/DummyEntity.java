package com.qldiem.demo.Repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DummyEntity {
    @Id
    public String id;
}
