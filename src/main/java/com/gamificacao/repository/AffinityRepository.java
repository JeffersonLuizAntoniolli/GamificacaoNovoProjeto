package com.gamificacao.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.Project;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;

@Repository
public interface AffinityRepository extends JpaRepository <Affinity, Long> {

}
