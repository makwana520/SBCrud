package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.Entity.UserRecord;

public interface UserRecordRepository extends JpaRepository<UserRecord, Integer> {
}
