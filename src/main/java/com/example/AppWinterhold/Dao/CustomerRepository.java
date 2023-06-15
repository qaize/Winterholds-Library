package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer,String> {

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
                WHERE c.membershipNumber LIKE %:number% AND (c.firstName LIKE %:name% OR c.lastName LIKE %:name%) 
            """)
    List<CustomerIndexDto> getListCustomerBySearch(String number, String name, Pageable paging);


    @Query("""
            SELECT COUNT(c.membershipNumber)
            FROM Customer AS c
            WHERE c.membershipNumber LIKE %:number% AND (c.firstName LIKE %:name% OR c.lastName LIKE %:name%) 
            """)
    Long getCountPage(String number, String name);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
            """)
    List<CustomerIndexDto> getAll();

    @Query("""
            SELECT DISTINCT  new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
                WHERE c.membershipExpireDate >  GETDATE() AND c.membershipNumber  NOT IN ( select c.membershipNumber
                                                                          from Customer AS c
                                                                          JOIN c.loan AS l
                                                                          WHERE  l.returnDate IS NULL AND l.denda != 0 AND l.loanDate != FORMAT(GETDATE(),'yyyy-MM-dd')) 
            """)
    List<CustomerIndexDto> getAvaliableCustomer();
    @Query("""
            SELECT DISTINCT  new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
                WHERE c.membershipExpireDate >  GETDATE() AND c.membershipNumber  = :member 
            """)
    List<CustomerIndexDto> getAvaliableCustomerEdit(String member);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
             WHERE c.membershipNumber = :customerNumber
            """)
    CustomerIndexDto getCustomerByMember(String customerNumber);

    @Query("""
            SELECT new com.example.AppWinterhold.Dto.Customer.CustomerInsertDto
            (
            
            c.membershipNumber,c.firstName,c.lastName,c.birthDate,
            c.gender,c.phone,c.address,c.membershipExpireDate
            )
            FROM Customer AS c
             WHERE c.membershipNumber = :number
            """)
    CustomerInsertDto getCustomerByMemberInsert(String number);

    @Query("""
            SELECT COUNT(l.id)
            FROM Loan AS l
              WHERE l.customerNumber = :number
            """)
    Long getCountCustomer(String number);

    @Query(nativeQuery = true,value = """
            update Customer\s
            set MembershipExpireDate = DATEADD(year,2,MembershipExpireDate)
            where MembershipNumber = :member
            """)
    void extend(String member);
}
