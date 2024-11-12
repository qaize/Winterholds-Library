package com.example.winterhold.service.abs;

import com.example.winterhold.dto.BaseResponseDTO;
import com.example.winterhold.dto.payment.TopUpDTO;

public interface TopUpService {
    BaseResponseDTO<Object> topup(TopUpDTO topupDTO);
}
