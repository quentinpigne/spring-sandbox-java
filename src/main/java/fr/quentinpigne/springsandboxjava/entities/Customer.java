package fr.quentinpigne.springsandboxjava.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customerSequence")
    @SequenceGenerator(name = "customerSequence", sequenceName = "customer_sequence", allocationSize = 1)
    private Long id;

    @Column(name = "code")
    private Integer code;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<CustomerIdentity> customerIdentityList;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    private CompanyInfo companyInfo;
}
