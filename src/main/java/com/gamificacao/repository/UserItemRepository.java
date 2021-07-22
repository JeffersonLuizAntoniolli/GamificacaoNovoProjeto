package com.gamificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gamificacao.model.UserAffinity;
import com.gamificacao.model.UserItems;

@Repository
public interface UserItemRepository extends JpaRepository<UserItems, Long> {

	 @Query("from UserItems ai where ai.user.id = :id") List<UserItems>
	 ListItemsByUser(@Param("id") Long id);
	 
}
