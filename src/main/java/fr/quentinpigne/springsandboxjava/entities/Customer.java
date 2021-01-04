package fr.quentinpigne.springsandboxjava.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

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
    private UUID id;

    @Column(name = "code")
    private Integer code;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    List<CustomerIdentity> customerIdentityList;

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL)
    CompanyInfo companyInfo;
}
