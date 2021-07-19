package com.gamificacao.service;

import java.util.List;

import com.gamificacao.model.Affinity;
import com.gamificacao.model.User;

public interface AffinityService {

    void createAffinity(Affinity affinity); // serviço para criar uma nova afinidades no sistema
 
    void updateAffinity(Long id, Affinity affinity); // serviço para editar dados de uma afinidades já cadastrada sistema

    void deleteAffinity(Long id);  // serviço para deletar uma afinidades no sistema

    List<Affinity> findAll();  // serviço para listar todas as afinidades do sistema
   
    Affinity getAffinityById(Long AffinityId); // serviço que busca uma afinidades pelo seu ID
}
