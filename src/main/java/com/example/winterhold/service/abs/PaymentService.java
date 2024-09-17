package com.example.winterhold.service.abs;

import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.payment.TopUpDTO;

public interface PaymentService {

    BaseResponseDTO<Object> enablePayment(String membershipNumber);

    BaseResponseDTO<Object> topup(TopUpDTO topupDTO);
}
