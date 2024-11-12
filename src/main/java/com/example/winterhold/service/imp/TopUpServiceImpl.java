package com.example.winterhold.service.imp;


import com.example.winterhold.constants.ActionConstants;
import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.payment.TopUpDTO;
import com.example.winterhold.entity.MasterAccount;
import com.example.winterhold.repository.MasterAccountRepository;
import com.example.winterhold.service.abs.TopUpService;
import com.example.winterhold.utility.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.Objects;

@Service
@Slf4j
public class TopUpServiceImpl implements TopUpService {

    private final MasterAccountRepository masterAccountRepository;

    public TopUpServiceImpl(MasterAccountRepository masterAccountRepository) {
        this.masterAccountRepository = masterAccountRepository;
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
                throw new NotFoundException("Master account not found");
            }

        } catch (Exception e) {
            log.info("[ERROR] Top up failed {}", e.getMessage());
            return ResponseUtil.insertFailResponse(e.getMessage());
        }

        return ResponseUtil.insertSuccessResponse(ActionConstants.SUCCESS);
    }



}
