package com.example.itemservice.domain;

import com.example.itemservice.handlers.Operation;
import lombok.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "person")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "person_id")
    @NotNull(message = "Id must be non null", groups = {
            Operation.OnUpdate.class, Operation.OnDelete.class
    })
    private int id;
    @NotNull(message = "Login must be non null", groups = {
            Operation.OnCreate.class
    })
    @Size(min = 3, max = 8, message = "Login must be more than 3 and less 8")
    @Column(name = "person_login")
    private String login;
    @NotNull(message = "Password must be non null")
    @Size(min = 3, max = 8, message = "Password must be more than 3 and less 8")
    @Column(name = "person_password")
    private String password;
    @NotNull()
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}