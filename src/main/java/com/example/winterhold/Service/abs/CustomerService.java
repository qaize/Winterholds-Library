package com.example.winterhold.Service.abs;

import com.example.winterhold.Dto.Customer.CustomerIndexDto;
import com.example.winterhold.Dto.Customer.CustomerInsertDto;
import com.example.winterhold.Dto.Customer.CustomerUpdateDto;
import com.example.winterhold.Dto.Models.DataDTO;
import com.example.winterhold.Entity.Customer;

import java.util.List;

public interface CustomerService {
    DataDTO<List<CustomerIndexDto>> getListCustomerBySearch(Integer page, String number, String name);

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
    DataDTO<List<Customer>> getBannedCustomerlist(Integer page);
    Customer getCustomerByEntity(String customerNumber);
    CustomerIndexDto getCustomerByMember(String customerNumber);
    CustomerUpdateDto getCustomerByMemberInsert(String number);
    Integer loanCountSetter(String customer, String action);
    List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber);
    void doUnbanCustomer(String customerNumber);
    Boolean doExtendMember(String number);
}
