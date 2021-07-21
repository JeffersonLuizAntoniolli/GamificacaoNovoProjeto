package com.gamificacao.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gamificacao.model.Item;
import com.gamificacao.model.User;
import com.gamificacao.model.UserItems;
import com.gamificacao.repository.ItemRepository;
import com.gamificacao.repository.UserItemRepository;
import com.gamificacao.repository.UserRepository;

@Service
public class ItemServiceImpl implements ItemService {
    private ItemRepository ItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserItemRepository userItemRepository;

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
	
	@Override
    public void buyItem(Long itemId, String userEmail) throws Exception {
    	User user = userRepository.findByEmail(userEmail);
    	Item item = this.getItemById(itemId);
    	
    	if(user.getPoints() < item.getCost()) {
    		throw new Exception("Você não possui pontos para comprar este item.");
    	}
    	
    	user.setPoints(user.getPoints()-item.getCost());
    	userRepository.save(user);
    	
    	UserItems userItem = new UserItems();
    	userItem.setItem(item);
    	userItem.setUser(user);
    	userItem.setUsed(false);
    	userItemRepository.save(userItem);

    }

}
