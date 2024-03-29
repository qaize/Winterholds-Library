package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CustomerRepository;
import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Dto.Loan.LoanInsertDto;
import com.example.AppWinterhold.Entity.Customer;
import com.example.AppWinterhold.Entity.Loan;
import com.example.AppWinterhold.Service.abs.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.AppWinterhold.Const.actionConst.*;

@Service
public class CustomerServiceImp implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private LoanRepository loanRepository;

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

            String generatedMember = customerNumberGenerator();
            LocalDateTime date = LocalDateTime.now();
            Customer en = new Customer(generatedMember, dto.getFirstName(), dto.getLastName(), dto.getBirthDate(),
                    dto.getGender(), dto.getPhone(), dto.getAddress(), dto.getMembershipExpireDate(), date, 0, 0,0);
            customerRepository.save(en);
            logService.saveLogs(CUSTOMER, SUCCESS, INSERT);
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER,FAILED, INSERT);
        }
    }

    @Override
    public void update(CustomerUpdateDto dto) {
        try {

            Customer en = mapCustomerUpdate(dto);
            customerRepository.save(en);
            logService.saveLogs(CUSTOMER, SUCCESS, UPDATE);
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER, e.getMessage(), UPDATE);
        }
    }

    private Customer mapCustomerUpdate(CustomerUpdateDto dto) {
        Optional<Customer> dataCus = customerRepository.findById(dto.getMembershipNumber());

        Customer customer = new Customer();
        if (dataCus.isPresent()) {

            dataCus.get().setFirstName(dto.getFirstName());
            dataCus.get().setLastName(dto.getLastName());
            dataCus.get().setGender(dto.getGender());
            dataCus.get().setPhone(dto.getPhone());
            dataCus.get().setAddress(dto.getAddress());
            dataCus.get().setMembershipExpireDate(dto.getMembershipExpireDate());
            customer = dataCus.get();
        }
        return customer;
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
    public CustomerUpdateDto getCustomerByMemberInsert(String number) {
        return customerRepository.getCustomerByMemberInsert(number);
    }

    @Override
    public Boolean delete(String number) {
        Long data = customerRepository.getCountCustomer(number);
        if (data > 0) {

            return false;
        }
        customerRepository.softDeleteCustomer(number);
        return true;
    }


    @Override
    public List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber) {
        return customerRepository.getAvaliableCustomerEdit(customerNumber);
    }

    @Override
    public void doUnbanCustomer(String customerNumber) {

        try {
            Optional<Customer> data = customerRepository.findById(customerNumber);
            Customer customer = new Customer();
            if (data.isPresent()) {
                customer = data.get();
                customer.setBanned(0);
                customerRepository.save(data.get());
                logService.saveLogs(CUSTOMER, SUCCESS, BAN);
            }
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER, e.getMessage(), BAN);

        }

    }

    @Override
    public Customer getCustomerByEntity(String customerNumber) {

        Optional<Customer> data = customerRepository.findById(customerNumber);
        Customer customer = new Customer();
        if (data.isPresent()) {
            customer = data.get();
        }
        return customer;
    }

    @Override
    public void updateWithEntity(Customer customer) {
        customerRepository.save(customer);
    }

    @Override
    public Integer loanCountSetter(String customer, String aReturn) {


        Integer data = customerRepository.getLoanCountCurrentCustomer(customer);
        if (aReturn.equals("Return")) {
            if (data != 0) {
                data = data - 1;
            }
        } else {
            data = data + 1;
        }
        return data;
    }

    @Override
    public String customerNumberGenerator() {
        int bound = 1000;
        Random randomer = new Random();
        int genratedValue = randomer.nextInt(bound);
        String genrator = "";
        boolean membershipChecker = true;
//        CHECK CUSTOMER IS ALREADY ON TABLE
        while (membershipChecker) {
            genrator = "CUS" + genratedValue;
            if (CustomerMemberChecker(genrator)) {
                genratedValue++;
            } else {
                genrator = "CUS" + genratedValue;
                membershipChecker = false;
            }
        }
        return genrator;
    }

    @Override
    public Boolean CustomerMemberChecker(String s) {
        Long result = customerRepository.checkCustomerById(s);
        return result > 0;
    }

    @Override
    public boolean doBanCustomer(String customerNumber) {
        boolean ban = true;
        try {
            Optional<Customer> data = customerRepository.findById(customerNumber);
            Long member = Long.valueOf(data.get().getMembershipNumber());
//            Check On Loan Customer
            Optional<Loan> dataLoan = loanRepository.findById(member);

            Customer customer = new Customer();

            if (data.isPresent() && dataLoan.isEmpty()) {
                customer = data.get();
                customer.setBanned(1);
                if (customer.getLoanCount() > 0) {
                    ban = false;
                } else {
                    customerRepository.save(customer);
                    logService.saveLogs(CUSTOMER, SUCCESS, BAN);
                }
            }
        } catch (Exception e) {
            logService.saveLogs(CUSTOMER, e.getMessage(), BAN);
            ban = false;
        }
        return ban;
    }

    @Override
    public List<Customer> getBannedCustomerlist(Integer page) {
        int dataCount = 10;
        Pageable pagination = PageRequest.of(page - 1, dataCount);

        return customerRepository.getBannedListCustomer(pagination);
    }

    @Override
    public Long getCountBannedList() {
        int dataCount = 10;
        Double data = customerRepository.getCountBannedCustomer();

        return (long) Math.ceil(data / dataCount);
    }


}
