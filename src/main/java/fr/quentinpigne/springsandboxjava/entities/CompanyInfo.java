package fr.quentinpigne.springsandboxjava.entities;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "company_info")
public class CompanyInfo {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    private Customer customer;

    @Column(name = "company_name")
    private String companyName;
}
