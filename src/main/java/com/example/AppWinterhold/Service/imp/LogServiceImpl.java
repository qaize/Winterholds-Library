package com.example.AppWinterhold.Service.imp;

import com.example.AppWinterhold.Controller.Model.BaseController;
import com.example.AppWinterhold.Dao.logsRepository;
import com.example.AppWinterhold.Entity.Logs;
import com.example.AppWinterhold.Service.abs.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    logsRepository logsRepo;

    @Autowired
    BaseController baseController;

    @Override
    public void saveLogs(String process, String status) {
        String createdBy = baseController.getCurrentLogin();
        LocalDate date = LocalDate.now();
        Logs logs = new Logs(UUID.randomUUID().toString(),process,status,createdBy,date);
        logsRepo.save(logs);
    }
}
