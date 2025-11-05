package com.vroomvroom.hub.domain.entity;

import com.vroomvroom.common.model.BaseTimeEntity;
import com.vroomvroom.hub.domain.vo.Address;
import com.vroomvroom.hub.domain.vo.Location;
import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Entity
@Table(name = "p_hub")
@EntityListeners(AuditingEntityListener.class)
public class Hub extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column
    private String hubName;

    @Embedded
    private Address address;

    @Embedded
    private Location location;
}
