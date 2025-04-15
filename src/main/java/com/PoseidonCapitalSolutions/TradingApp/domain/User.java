package com.PoseidonCapitalSolutions.TradingApp.domain;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "username", length = 125, nullable = false)
    private String username;

    @Column(name = "password", length = 125, nullable = false)
    private String password;

    @Column(name = "fullname", length = 125)
    private String fullname;

    @Column(name = "role", length = 125, nullable = false)
    private String role;

}
