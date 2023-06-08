package com.example.AppWinterhold.Entity;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Entity
@Table(name = "Customer")
public class Customer {

    @Id
    @Column(name = "MembershipNumber")
    private String membershipNumber; /*20 nn*/
    @Column(name = "FirstName")
    private String firstName;/*50 nn*/
    @Column(name = "LastName")
    private String lastName;/*50 n*/
    @Column(name = "BirthDate")
    private LocalDate birthDate;/*nn*/
    @Column(name = "Gender")
    private String gender;/*10 nn*/
    @Column(name = "Phone")
    private String phone;/*20 n*/
    @Column(name = "Address")
    private String address;/*500 n*/
    @Column(name = "MembershipExpireDate")
    private LocalDate membershipExpireDate;/* nn*/

    @OneToMany
    @JoinColumn(name="CustomerNumber")
    private List<Loan> loan;

    public Customer(String membershipNumber, String firstName, String lastName, LocalDate birthDate, String gender, String phone, String address, LocalDate membershipExpireDate) {
        this.membershipNumber = membershipNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
        this.address = address;
        this.membershipExpireDate = membershipExpireDate;
    }
}