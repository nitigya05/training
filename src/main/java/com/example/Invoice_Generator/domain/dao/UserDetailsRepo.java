package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsRepo extends JpaRepository<UserDetails,Integer> {
}
