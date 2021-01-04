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
@Table(name = "company_info")
public class CompanyInfo {

    @Id
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Customer customer;

    @Column(name = "company_name")
    private String companyName;
}
