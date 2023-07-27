package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Entity.LogsIncome;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LogsIncomeRepository extends JpaRepository<LogsIncome,String> {

   @Query("""
           SELECT li
           FROM LogsIncome as li
           """)
    List<LogsIncome> getPageOnPaymentHistory(Pageable paging);


   @Query("""
           SELECT count(uuid)
           FROM LogsIncome as li
           """)
    Long getCountTotalPaymentHistory();

//    @Query("""
//            SELECT TOP 1 li
//            FROM LogsIncome as li
//            ORDER BY transactionDate DESC
//            """)
//    LogsIncome findLatestData();
}
