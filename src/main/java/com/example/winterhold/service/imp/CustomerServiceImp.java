package com.example.winterhold.service.imp;

import com.example.winterhold.dto.account.AccountInsertDto;
import com.example.winterhold.repository.CustomerRepository;
import com.example.winterhold.repository.MasterAccountRepository;
import com.example.winterhold.dto.customer.CustomerIndexDto;
import com.example.winterhold.dto.customer.CustomerInsertDto;
import com.example.winterhold.dto.customer.CustomerProfileDto;
import com.example.winterhold.dto.customer.CustomerUpdateDto;
import com.example.winterhold.dto.models.DataDTO;
import com.example.winterhold.entity.Customer;
import com.example.winterhold.entity.MasterAccount;
import com.example.winterhold.service.abs.CustomerService;
import com.example.winterhold.utility.CommonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.winterhold.constants.ActionConstants.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImp implements CustomerService {

    private final CustomerRepository customerRepository;
    private final LogServiceImpl logService;
    private final AccountServiceImp accountServiceImp;
    private final MasterAccountRepository masterAccountRepository;
    private final Random random = new Random();

    @Override
    public DataDTO<List<CustomerIndexDto>> getListCustomerBySearch(Integer page, String number, String name) {
        int row = 10;
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
        return (long) Math.ceil(totalData / row);
    }

    @Override
    public void insertNewCustomer(CustomerInsertDto customerInsert) {
        log.info("insert new customer {}", customerInsert.getFirstName());
        try {
            Customer newCustomer = populateCustomerData(customerInsert);
            AccountInsertDto accountInsertDto = populateAccountInsertDto(newCustomer);

            customerRepository.save(newCustomer);
            accountServiceImp.createNewAccount(accountInsertDto);
            log.info(SUCCESS_INSERT_DATA, newCustomer.getMembershipNumber());
            logService.saveLogs(CUSTOMER, SUCCESS, INSERT);
        } catch (Exception e) {
            log.error(FAILED_INSERT_DATA, e.getMessage());
            logService.saveLogs(CUSTOMER, FAILED, INSERT);
        }
    }

    private AccountInsertDto populateAccountInsertDto(Customer newCustomer) {
        return AccountInsertDto.builder()
                .username(newCustomer.getMembershipNumber())
                .name(newCustomer.getFirstName() + " " + (Objects.nonNull(newCustomer.getLastName()) ? newCustomer.getLastName() : ""))
                .role("customer")
                .password("newCustomer")
                .build();
    }


    @Override
    public void update(CustomerUpdateDto dto) {
        try {
            customerRepository.save(mapUpdate(dto));
            log.info(SUCCESS_UPDATE_DATA, dto.getMembershipNumber());
            logService.saveLogs(CUSTOMER, SUCCESS, UPDATE);
        } catch (Exception e) {
            log.error(FAILED_UPDATE_DATA, e.getMessage());
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
        CustomerIndexDto customerDetail = new CustomerIndexDto();
        try {
            customerDetail =  customerRepository.getCustomerByMember(customerNumber);

        } catch (Exception e){
            log.error(e.getMessage(), e);
        }

        return customerDetail;
    }

    @Override
    public CustomerUpdateDto getCustomerByMemberInsert(String number) {
        return customerRepository.getCustomerByMemberInsert(number);
    }

    @Override
    public Boolean delete(String number) {
        try {
            Long data = customerRepository.getCountCustomer(number);
            if (data > 0) {
                customerRepository.softDeleteCustomer(number);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }


    @Override
    public List<CustomerIndexDto> getAvaliableCustomerEdit(String customerNumber) {
        return customerRepository.getAvaliableCustomerEdit(customerNumber);
    }

    @Override
    public Boolean doUnbanCustomer(String customerNumber) {

        try {
            log.info("Start Unban Customer [{}]", customerNumber);
            Optional<Customer> data = customerRepository.findById(customerNumber);
            Customer customer;
            if (data.isPresent()) {
                customer = data.get();
                customer.setBanned(0);
                customerRepository.save(customer);
                logService.saveLogs(CUSTOMER, SUCCESS, BAN);
                return true;
            }
            log.info("Customer [{}] Not Found", customerNumber);
        } catch (Exception e) {
            log.error("Error Unban Customer [{}]", customerNumber, e);
            logService.saveLogs(CUSTOMER, e.getMessage(), BAN);
        }
        return false;
    }

    @Override
    public Boolean doExtendMember(String number) {
        try {

            CustomerUpdateDto data = getCustomerByMemberInsert(number);

            data.setMembershipExpireDate(data.getMembershipExpireDate().plusYears(2));
            update(data);
            log.info(SUCCESS_UPDATE_DATA, number);
            logService.saveLogs(CUSTOMER, SUCCESS, EXTEND);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage());
            logService.saveLogs(CUSTOMER, FAILED, EXTEND);
            return false;
        }
    }

    @Override
    public CustomerProfileDto customerProfile(String username) {

        CustomerProfileDto customerProfileDto = new CustomerProfileDto();
        try {

            String balance = StringUtils.EMPTY;
            var customer = customerRepository.findByMembershipNumber(username).orElseThrow();
            String birthDate = CommonUtil.convertBirthdateIdn(customer.getBirthDate());

            if (Objects.nonNull(customer.getIsRegistered())) {
                MasterAccount masterAccount = masterAccountRepository.findMasterAccountByMembershipNumber(customer.getMembershipNumber());
                balance = String.valueOf(masterAccount.getBalance());
            }

            customerProfileDto = CustomerProfileDto.builder()
                    .membershipNumber(customer.getMembershipNumber())
                    .fullName(customer.getFirstName().concat(StringUtils.SPACE).concat(customer.getLastName()))
                    .phone(customer.getPhone())
                    .address(customer.getAddress())
                    .birthDate(birthDate)
                    .gender(customer.getGender())
                    .balance(balance)
                    .build();

            return customerProfileDto;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return customerProfileDto;

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
    public String customerNumberGenerator() {
        int boundNumber = 1000;
        int genratedValue = random.nextInt(boundNumber);
        String newCustomerId = "";
        boolean membershipChecker = true;

        //Check Customer Availability
        while (membershipChecker) {
            newCustomerId = CUSTOMER_ID_PREFIX + genratedValue;
            if (Boolean.TRUE.equals(customerAvailabilityCheck(newCustomerId))) {
                genratedValue++;
            } else {
                newCustomerId = CUSTOMER_ID_PREFIX + genratedValue;
                membershipChecker = false;
            }
        }
        return newCustomerId;
    }

    @Override
    public Boolean customerAvailabilityCheck(String s) {
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
                throw new NotFoundException("User not found");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
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
            log.error(e.getMessage());

            return DataDTO.<List<Customer>>builder()
                    .data(new ArrayList<>())
                    .totalPage(0L)
                    .build();
        }
    }


    private Customer populateCustomerData(CustomerInsertDto dto) {

        String generatedMember = customerNumberGenerator();
        LocalDateTime createDate = LocalDateTime.now();

        return new Customer(
                generatedMember,
                dto.getFirstName(),
                dto.getLastName(),
                dto.getBirthDate(),
                dto.getGender(),
                dto.getPhone(),
                dto.getAddress(),
                dto.getMembershipExpireDate(),
                createDate, 0, 0, 0, 0);
    }


    private Customer mapUpdate(CustomerUpdateDto dto) {
        Optional<Customer> dataCus = customerRepository.findById(dto.getMembershipNumber());

        Customer customer = new Customer();
        if (dataCus.isPresent()) {
            dataCus.get().setFirstName(dto.getFirstName());
            dataCus.get().setLastName(dto.getLastName());
            dataCus.get().setGender(dto.getGender());
            dataCus.get().setBirthDate(dto.getBirthDate());
            dataCus.get().setPhone(dto.getPhone());
            dataCus.get().setAddress(dto.getAddress());
            dataCus.get().setMembershipExpireDate(dto.getMembershipExpireDate());
            customer = dataCus.get();
        }
        return customer;
    }

}
