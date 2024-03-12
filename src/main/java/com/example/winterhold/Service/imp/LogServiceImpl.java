package com.example.winterhold.Service.imp;

import com.example.winterhold.Controller.Model.BaseController;
import com.example.winterhold.Dao.LogRepository;
import com.example.winterhold.Entity.Logs;
import com.example.winterhold.Service.abs.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    LogRepository logsRepo;

    @Autowired
    BaseController baseController;

    @Override
    public void saveLogs(String process, String status, String action) {
        String createdBy = baseController.getCurrentLogin();
        LocalDateTime date = LocalDateTime.now();
        Logs logs = new Logs(UUID.randomUUID().toString(),process,status,createdBy,date,action);
        logsRepo.save(logs);
    }
}
