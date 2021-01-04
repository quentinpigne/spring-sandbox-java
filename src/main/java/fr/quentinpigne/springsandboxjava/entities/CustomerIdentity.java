package fr.quentinpigne.springsandboxjava.entities;

import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_identity")
public class CustomerIdentity {

    @Id
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "phone")
    private String phone;

    @Column(name = "mobile")
    private String mobile;
}
