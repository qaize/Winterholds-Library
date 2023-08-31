package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Entity.Customer;

import java.util.List;

public interface CustomerService {
    List<CustomerIndexDto> getListCustomerBySearch(Integer page, String number, String name);

    Long getCountBannedList();
    Boolean delete(String number);
    List<CustomerIndexDto> getAll();
    String customerNumberGenerator();
    void update(CustomerUpdateDto dto);
    void insert(CustomerInsertDto dto);
    Boolean CustomerMemberChecker(String s);
    void updateWithEntity(Customer customer);
    boolean doBanCustomer(String customerNumber);
    List<CustomerIndexDto> getAvaliableCustomer();
    Long getCountPage(String number, String name);
    List<Customer> getBannedCustomerlist(Integer page);
    Customer getCustomerByEntity(String customerNumber);
    CustomerIndexDto getCustomerByMember(String customerNumber);
    CustomerUpdateDto getCustomerByMemberInsert(String number);
    Integer loanCountSetter(String customer, String action);
    List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber);

    void doUnbanCustomer(String customerNumber);
}
