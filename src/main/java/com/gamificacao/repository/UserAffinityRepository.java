package com.gamificacao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamificacao.model.UserAffinity;

@Repository
public interface UserAffinityRepository extends JpaRepository<UserAffinity, Long> {

  //  User findByEmail(String email);
}
