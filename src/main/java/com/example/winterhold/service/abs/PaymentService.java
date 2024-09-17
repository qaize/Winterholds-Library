package com.example.winterhold.service.abs;

import com.example.winterhold.dto.BaseResponseDTO;

public interface PaymentService {

    BaseResponseDTO<Object> enablePayment(String membershipNumber);
}
