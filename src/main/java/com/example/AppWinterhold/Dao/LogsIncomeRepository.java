package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Entity.LogsIncome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LogsIncomeRepository extends JpaRepository<LogsIncome,String> {

//    @Query("""
//            SELECT TOP 1
//            FROM logsIncome as i
//            order by
//            """)
//    LogsIncome findLatestData();
}
