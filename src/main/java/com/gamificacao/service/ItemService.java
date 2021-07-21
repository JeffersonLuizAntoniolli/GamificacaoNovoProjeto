package com.gamificacao.service;

import java.util.List;

import com.gamificacao.model.Item;
import com.gamificacao.model.User;

public interface ItemService {

    void createItem(Item item); // serviço para criar um novo item no sistema
 
    void updateItem(Long id, Item item); // serviço para editar dados de um item já cadastrado sistema

    void deleteItem(Long id);  // serviço para deletar um item no sistema

    List<Item> findAll();  // serviço para listar todos os itens do sistema
   
    Item getItemById(Long ItemId); // serviço que busca um item pelo seu ID
    
    void buyItem(Long itemId, String userEmail) throws Exception; // realiza a compra do item pelo usuário
}
