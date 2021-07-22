package com.gamificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.model.UserAffinity;

@Repository
public interface UserAffinityRepository extends JpaRepository<UserAffinity, Long> {

	@Query("from UserAffinity af where af.affinity.id= :id and af.user.id = :user_id")
	UserAffinity findByForeignKeysId(@Param("id") Long id, @Param("user_id") Long user_id); // busca afinidade do usuario pela atividade
	
	@Query("from UserAffinity af where af.user.id = :id")
	List<UserAffinity> ListAffinityByUser(@Param("id") Long id);
}
