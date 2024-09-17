package com.example.winterhold.service.imp;

import com.example.winterhold.constants.ActionConstants;
import com.example.winterhold.dto.payment.TopUpDTO;
import com.example.winterhold.repository.CustomerRepository;
import com.example.winterhold.repository.MasterAccountRepository;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.customer.CustomerProfileDto;
import com.example.winterhold.entity.Customer;
import com.example.winterhold.entity.MasterAccount;
import com.example.winterhold.service.abs.PaymentService;
import com.example.winterhold.utility.CommonUtil;
import com.example.winterhold.utility.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final MasterAccountRepository masterAccountRepository;

    private final CustomerRepository customerRepository;

    @Override
    public BaseResponseDTO<Object> enablePayment(String membershipNumber) {

        CustomerProfileDto customerProfile;
        try {
            log.info("[START] Enable payment for {}",membershipNumber);
            var customer = customerRepository.findByMembershipNumber(membershipNumber).get();

            if(Objects.isNull(customer.getIsRegistered()) || customer.getIsRegistered().equals(0)){
                customer.setIsRegistered(1);
            }else {
                throw new RuntimeException("current membership already registered");
            }

            MasterAccount masterAccount = MasterAccount.builder()
                    .membershipNumber(customer.getMembershipNumber())
                    .balance(0)
                    .build();

            masterAccountRepository.save(masterAccount);
            customerRepository.save(customer);

            log.info("[END] Success add payment for {}",membershipNumber);
            customerProfile = populateCustomerInfo(customer,masterAccount);

        } catch (Exception e){
            log.error("[Error] Unable to insert to db : {}",e.getMessage());
            return ResponseUtil.insertFailResponse(e.getMessage());
        }

        return ResponseUtil.insertSuccessResponse(customerProfile);
    }

    @Override
    public BaseResponseDTO<Object> topup(TopUpDTO topupDTO) {

        try {
            log.info("[START] Top up for {}",topupDTO.getMembershipNumber());
            MasterAccount masterAccount = masterAccountRepository.findMasterAccountByMembershipNumber(topupDTO.getMembershipNumber());
            if (Objects.nonNull(masterAccount)) {
                masterAccount.setBalance(masterAccount.getBalance() + topupDTO.getAmount());
            } else {
                log.info("[ERROR] Master account not found");
                throw new RuntimeException("Master account not found");
            }

        } catch (Exception e) {
            return ResponseUtil.insertFailResponse(e.getMessage());
        }

        return ResponseUtil.insertSuccessResponse(ActionConstants.SUCCESS);
    }

    private CustomerProfileDto populateCustomerInfo(Customer customer,MasterAccount masterAccount){
        String birthDate = CommonUtil.convertBirthdateIdn(customer.getBirthDate());

        return CustomerProfileDto.builder()
                .membershipNumber(customer.getMembershipNumber())
                .fullName(customer.getFirstName().concat(StringUtils.SPACE).concat(customer.getLastName()))
                .phone(customer.getPhone())
                .address(customer.getAddress())
                .birthDate(birthDate)
                .gender(customer.getGender())
                .balance(String.valueOf(masterAccount.getBalance()))
                .build();
    }
}
