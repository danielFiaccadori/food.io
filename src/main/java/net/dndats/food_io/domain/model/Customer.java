package net.dndats.food_io.domain.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "customers")
public class Customer {

    @Id @Column(unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 100)
    private String phoneNumber;

    @Column(nullable = false, length = 100)
    private String address;

}
