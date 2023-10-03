package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Dao.CustomerRepository;
import com.example.AppWinterhold.Dao.LoanRepository;
import com.example.AppWinterhold.Dto.Customer.CustomerIndexDto;
import com.example.AppWinterhold.Dto.Customer.CustomerInsertDto;
import com.example.AppWinterhold.Dto.Customer.CustomerUpdateDto;
import com.example.AppWinterhold.Dto.Models.DataDTO;
import com.example.AppWinterhold.Entity.Customer;
import com.example.AppWinterhold.Service.abs.CustomerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static com.example.AppWinterhold.Const.actionConst.*;

@Service
public class CustomerServiceImp implements CustomerService {

    private final static Logger LOGGER = LoggerFactory.getLogger(CustomerServiceImp.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LogServiceImpl logService;

    @Autowired
    private LoanRepository loanRepository;

    @Override
    public DataDTO<List<CustomerIndexDto>> getListCustomerBySearch(Integer page, String number, String name) {
        Integer row = 10;
        int flag = 0;
        String message = "";

        try {
            Pageable paging = PageRequest.of(page - 1, row, Sort.by("membershipNumber"));
            Page<CustomerIndexDto> customerData = customerRepository.getListCustomerBySearch(number, name, paging);

            if (customerData.isEmpty()) {
                flag = 1;
                message = INDEX_EMPTY;
            }

            return DataDTO.<List<CustomerIndexDto>>builder()
                    .flag(flag)
                    .totalPage((long) customerData.getTotalPages())
                    .message(message)
                    .data(customerData.getContent())
                    .build();

        } catch (Exception e) {
            return DataDTO.<List<CustomerIndexDto>>builder()
                    .flag(flag)
                    .message(message)
                    .build();
        }
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
            Customer newCustomer = mapInsertAdmin(dto);
            customerRepository.save(newCustomer);
            LOGGER.info(SUCCESS_INSERT_DATA, newCustomer.getMembershipNumber());
            logService.saveLogs(CUSTOMER, SUCCESS, INSERT);
        } catch (Exception e) {
            LOGGER.error(FAILED_INSERT_DATA, e.getMessage());
            logService.saveLogs(CUSTOMER, FAILED, INSERT);
        }
    }


    @Override
    public void update(CustomerUpdateDto dto) {
        try {
            customerRepository.save(mapUpdate(dto));
            LOGGER.info(SUCCESS_UPDATE_DATA, dto.getMembershipNumber());
            logService.saveLogs(CUSTOMER, SUCCESS, UPDATE);
        } catch (Exception e) {
            LOGGER.error(FAILED_UPDATE_DATA, e.getMessage());
            logService.saveLogs(CUSTOMER, FAILED, UPDATE);
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
    public CustomerUpdateDto getCustomerByMemberInsert(String number) {
        return customerRepository.getCustomerByMemberInsert(number);
    }

    @Override
    public Boolean delete(String number) {
        try {
            Long data = customerRepository.getCountCustomer(number);
            return data > 0 ? doDelete(number) : false;
        } catch (Exception e) {
            return false;
        }
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
    public Boolean doExtendMember(String number) {
        try {

            CustomerUpdateDto data = getCustomerByMemberInsert(number);

            data.setMembershipExpireDate(data.getMembershipExpireDate().plusYears(2));
            update(data);
            LOGGER.info(SUCCESS_UPDATE_DATA, number);
            logService.saveLogs(CUSTOMER, SUCCESS, EXTEND);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            logService.saveLogs(CUSTOMER, FAILED, EXTEND);
            return false;
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
    public Integer loanCountSetter(String customer, String flags) {

        Integer data = customerRepository.getLoanCountCurrentCustomer(customer);
        if (flags.equals("Return")) {
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

        try {
            Optional<Customer> data = customerRepository.findById(customerNumber);
            if (data.isPresent()) {
                Customer customer = data.get();
                if (customer.getLoanCount() > 0) {
                    return false;
                } else {
                    customer.setBanned(1);
                    customerRepository.save(customer);
                    logService.saveLogs(CUSTOMER, SUCCESS, BAN);
                    return true;
                }
            } else {
                throw new Exception("User not found");
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            logService.saveLogs(CUSTOMER, e.getMessage(), BAN);
            return false;
        }
    }

    @Override
    public DataDTO<List<Customer>> getBannedCustomerlist(Integer page) {
        int dataCount = 5;
        try {
            Pageable pagination = PageRequest.of(page - 1, dataCount);
            Page<Customer> bannedCustomer = customerRepository.getBannedListCustomer(pagination);

            return DataDTO.<List<Customer>>builder()
                    .data(bannedCustomer.getContent())
                    .totalPage((long) bannedCustomer.getTotalPages())
                    .build();

        } catch (Exception e) {
            LOGGER.error(e.getMessage());

            return DataDTO.<List<Customer>>builder()
                    .data(new ArrayList<>())
                    .totalPage(0L)
                    .build();
        }
    }


    private Customer mapInsertAdmin(CustomerInsertDto dto) {

        String generatedMember = customerNumberGenerator();
        LocalDateTime createDate = LocalDateTime.now();
        Customer customer = new Customer(
                generatedMember,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate(),
                dto.getGender(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getMembershipExpireDate(),
                createDate, 0, 0, 0,0);

        return customer;
    }


    private Customer mapUpdate(CustomerUpdateDto dto) {
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

    private Boolean doDelete(String member) {
        customerRepository.softDeleteCustomer(member);
        return true;
    }

}
