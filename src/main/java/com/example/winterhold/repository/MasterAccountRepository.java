package com.example.winterhold.repository;

import com.example.winterhold.entity.MasterAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MasterAccountRepository extends JpaRepository<MasterAccount,Long> {
    MasterAccount findMasterAccountByMembershipNumber(String membershipNumber);

}
