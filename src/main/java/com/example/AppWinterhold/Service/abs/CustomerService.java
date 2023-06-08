package com.example.AppWinterhold.Service.abs;

import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;

import java.util.List;

public interface CustomerService {
    List<CustomerIndexDto> getListCustomerBySearch(Integer page, String number, String name);

    Long getCountPage(String number, String name);

    void insert(CustomerInsertDto dto);

    List<CustomerIndexDto> getAll();

    List<CustomerIndexDto> getAvaliableCustomer();

    CustomerIndexDto getCustomerByMember(String customerNumber);

    CustomerInsertDto getCustomerByMemberInsert(String number);

    Boolean delete(String number);

    void update(CustomerUpdateDto dto);

    List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber);
}