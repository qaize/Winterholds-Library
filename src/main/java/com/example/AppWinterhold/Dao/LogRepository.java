package com.example.AppWinterhold.Dao;

import com.example.AppWinterhold.Entity.Logs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface LogRepository extends JpaRepository<Logs,String> {

}
