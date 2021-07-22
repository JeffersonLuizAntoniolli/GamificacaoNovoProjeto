package com.gamificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

   User findByEmail(String email);
   
   List<User> findByOrderByExperienceDesc();
   List<User> findByOrderByPointsDesc();
   
}
