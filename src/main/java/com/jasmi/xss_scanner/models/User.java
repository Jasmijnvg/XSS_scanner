package com.jasmi.xss_scanner.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;
    private String userName;
    private String password;
    private Boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
