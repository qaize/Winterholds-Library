package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Transactional
@Repository
public interface CustomerRepository extends JpaRepository<Customer, String> {

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
                ( c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                  c.gender,c.phone,c.address,c.membershipExpireDate )
            FROM 
                Customer AS c
            WHERE 
                c.membershipNumber LIKE %:number% 
                AND (c.firstName LIKE %:name% OR c.lastName LIKE %:name%) 
                AND c.banned = 0 AND c.deleted = 0
            """)
    Page<CustomerIndexDto> getListCustomerBySearch(String number, String name, Pageable paging);


    @Query("""
            SELECT 
                COUNT(c.membershipNumber)
            FROM 
                Customer AS c
            WHERE 
                c.membershipNumber LIKE %:number% 
                AND (c.firstName LIKE %:name% OR c.lastName LIKE %:name%) 
                AND c.deleted = 0 
            """)
    Long getCountPage(String number, String name);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
                (   c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                    c.gender,c.phone,c.address,c.membershipExpireDate   )
            FROM 
                Customer AS c
            """)
    List<CustomerIndexDto> getAll();

    @Query("""
            SELECT DISTINCT  
                new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
                ( c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                c.gender,c.phone,c.address,c.membershipExpireDate   )
            FROM 
                Customer AS c
            WHERE 
                c.banned = 0 
                AND c.membershipExpireDate >  GETDATE() 
                AND c.membershipNumber  NOT IN 
                                            (SELECT 
                                                c.membershipNumber
                                             FROM 
                                                Customer AS c
                                                             JOIN c.loan AS l
                                             WHERE 
                                                l.returnDate IS NULL 
                                                AND l.denda != 0 
                                                AND l.loanDate != FORMAT(GETDATE(),'yyyy-MM-dd'))
                                                 
            """)
    List<CustomerIndexDto> getAvaliableCustomer();

    @Query("""
            SELECT DISTINCT
                new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
                (   c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                    c.gender,c.phone,c.address,c.membershipExpireDate   )
            FROM 
                Customer AS c
            WHERE 
                c.membershipExpireDate >  GETDATE() 
                AND c.membershipNumber = :member 
            """)
    List<CustomerIndexDto> getAvaliableCustomerEdit(@Param("member")String member);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Customer.CustomerIndexDto
                (   c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                    c.gender,c.phone,c.address,c.membershipExpireDate   )
            FROM 
                Customer AS c
            WHERE 
                c.membershipNumber = :customerNumber
            """)
    CustomerIndexDto getCustomerByMember(@Param("customerNumber")String customerNumber);

    @Query("""
            SELECT 
                new com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto
                (   c.membershipNumber,c.firstName,c.lastName,c.birthDate,
                    c.gender,c.phone,c.address,c.membershipExpireDate   )
            FROM 
                Customer AS c
            WHERE 
                c.membershipNumber = :number
            """)
    CustomerUpdateDto getCustomerByMemberInsert(@Param("number")String number);

    @Query("""
            SELECT 
                COUNT(l.id)
            FROM 
                Loan AS l
            WHERE 
                l.customerNumber = :number 
            """)
    Long getCountCustomer(String number);


    @Query(nativeQuery = true, value = """
            update Customer\s
            set MembershipExpireDate = DATEADD(year,2,MembershipExpireDate)
            where MembershipNumber = :member
            """)
    void extend(String member);

    @Query("""
            SELECT 
                COUNT(c.membershipNumber)
            FROM 
                Customer AS c
            WHERE 
                c.membershipNumber = :number
            """)
    Long checkCustomerById(@Param("number") String s);

    @Query("""
            SELECT 
                c.loanCount
            FROM 
                Customer as c
            WHERE 
                c.membershipNumber = :member
            """)
    Integer getLoanCountCurrentCustomer(@Param("member") String membershipNumber);

    @Query("""
            SELECT 
                c
            FROM 
                Customer as c
            WHERE 
                c.banned = 1
            """)
    Page<Customer> getBannedListCustomer(Pageable pagination);


    @Query(nativeQuery = true,value = """
            update Customer\s
            set deleted = 1
            where MembershipNumber = :number
            """)
    void softDeleteCustomer(String number);
}
