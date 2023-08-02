package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CustomerRepository;
import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Entity.Customer;
import com.example.AppWinterhold.Service.abs.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.AppWinterhold.Const.actionConst.*;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LogServiceImpl logService;

    @Override
    public List<CustomerIndexDto> getListCustomerBySearch(Integer page, String number, String name) {
        Integer row = 10;
        Pageable paging = PageRequest.of(page - 1, row, Sort.by("membershipNumber"));
        return customerRepository.getListCustomerBySearch(number, name, paging);
    }

    @Override
    public Long getCountPage(String number, String name) {
        Integer row = 10;
        Double totalData = (double) customerRepository.getCountPage(number, name);
        Long totaPage = (long) Math.ceil(totalData / row);
        return totaPage;
    }

    @Override
    public void insert(CustomerInsertDto dto) {
        try {
            LocalDateTime date = LocalDateTime.now();
            Customer en = new Customer(dto.getMembershipNumber(), dto.getFirstName(), dto.getLastName(), dto.getBirthDate(),
                    dto.getGender(), dto.getPhone(), dto.getAddress(), dto.getMembershipExpireDate(), date, 0);
            customerRepository.save(en);
            logService.saveLogs(CUSTOMER, SUCCESS, INSERT);
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER, e.getMessage(), INSERT);
        }
    }

    @Override
    public List<CustomerIndexDto> getAll() {
        return customerRepository.getAll();
    }

    @Override
    public List<CustomerIndexDto> getAvaliableCustomer() {
        return customerRepository.getAvaliableCustomer();
    }

    @Override
    public CustomerIndexDto getCustomerByMember(String customerNumber) {
        return customerRepository.getCustomerByMember(customerNumber);
    }

    @Override
    public CustomerInsertDto getCustomerByMemberInsert(String number) {
        return customerRepository.getCustomerByMemberInsert(number);
    }

    @Override
    public Boolean delete(String number) {
        Long data = customerRepository.getCountCustomer(number);
        if (data > 0) {

            return false;
        }
        customerRepository.deleteById(number);
        return true;
    }

    @Override
    public void update(CustomerUpdateDto dto) {
        try {

            Integer lastLoanCount = customerRepository.getLoanCountCurrentCustomer(dto.getMembershipNumber());
            LocalDateTime datenow = LocalDateTime.now();
            Customer en = new Customer(dto.getMembershipNumber(), dto.getFirstName(), dto.getLastName(), dto.getBirthDate(),
                    dto.getGender(), dto.getPhone(), dto.getAddress(), dto.getMembershipExpireDate(), datenow, lastLoanCount);
            customerRepository.save(en);
            logService.saveLogs(CUSTOMER, SUCCESS, INSERT);
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER, e.getMessage(), INSERT);

        }
    }

    @Override
    public List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber) {
        return customerRepository.getAvaliableCustomerEdit(customerNumber);
    }

    @Override
    public Customer getCustomerByEntity(String customerNumber) {
        return customerRepository.findById(customerNumber).get();
    }

    @Override
    public void updateWithEntity(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Integer loanCountSetter(String customerNumber, String aReturn) {

        Integer data = customerRepository.getLoanCountCurrentCustomer(customerNumber);
        if(aReturn.equals("Return")){
            if(data!=0){
            data = data - 1;
            }
        }else{
            data = data + 1;
        }
        return data;
    }
}
