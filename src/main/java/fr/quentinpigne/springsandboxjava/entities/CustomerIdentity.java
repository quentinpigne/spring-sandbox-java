package fr.quentinpigne.springsandboxjava.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer_identity")
public class CustomerIdentity {

    @Id
    @Column(name = "customer_identity_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerIdentitySequence")
    @SequenceGenerator(name = "customerIdentitySequence", sequenceName = "customer_identity_sequence", allocationSize = 1)
    private Long customerIdentityId;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
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
