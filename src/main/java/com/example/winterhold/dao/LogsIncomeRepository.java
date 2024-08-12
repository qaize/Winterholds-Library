package com.example.winterhold.dao;

import com.example.winterhold.entity.LogsIncome;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface LogsIncomeRepository extends JpaRepository<LogsIncome,String> {

   @Query("""
           SELECT 
                li
           FROM 
                LogsIncome as li
           """)
    List<LogsIncome> getPageOnPaymentHistory(Pageable paging);

   @Query("""
           SELECT 
                count(uuid)
           FROM 
                LogsIncome as li
           """)
    Long getCountTotalPaymentHistory();

}
