package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.Dao.LogRepository;
import com.example.AppWinterhold.Entity.Logs;
import com.example.AppWinterhold.Service.abs.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
