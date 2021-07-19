package com.gamificacao.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Item;
import com.gamificacao.model.Task;
import com.gamificacao.model.User;
import com.gamificacao.repository.ItemRepository;
import com.gamificacao.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository ItemRepository;

    @Autowired
    public ItemServiceImpl(ItemRepository ItemRepository) {
        this.ItemRepository = ItemRepository;
    }
    
	@Override
	public void createItem(Item Item) {
		ItemRepository.save(Item);
	}

	@Override
	public void updateItem(Long id, Item updateditem) {
		Item item = ItemRepository.getOne(id);
		item.setName(updateditem.getName());
		item.setDescription(updateditem.getDescription());
		item.setCost(updateditem.getCost());
        ItemRepository.save(item);
	}

	@Override
	public void deleteItem(Long id) {
		ItemRepository.deleteById(id);
	}

	@Override
	public List<Item> findAll() {
		return ItemRepository.findAll();
	}

	@Override
	public Item getItemById(Long itemId) {
		return ItemRepository.findById(itemId).orElse(null);
	}

    

}
