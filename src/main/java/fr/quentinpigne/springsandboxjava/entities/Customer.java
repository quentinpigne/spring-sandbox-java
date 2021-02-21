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

    @OneToOne(mappedBy = "customer", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private CompanyInfo companyInfo;

    public void setCompanyInfo(CompanyInfo companyInfo) {
        if (companyInfo == null) {
            if (this.companyInfo != null) {
                this.companyInfo.setCustomer(null);
            }
        } else {
            companyInfo.setCustomer(this);
        }
        this.companyInfo = companyInfo;
    }
}
