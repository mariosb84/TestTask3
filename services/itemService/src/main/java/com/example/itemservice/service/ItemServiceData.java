package com.example.itemservice.service;

import com.example.itemservice.domain.dto.ItemDto;
import com.example.itemservice.domain.model.Item;
import com.example.itemservice.domain.model.Status;
import com.example.itemservice.domain.model.User;
import com.example.itemservice.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.itemservice.domain.model.Status.Draft;

@Service
@AllArgsConstructor
public class ItemServiceData implements ItemService {

    private final ItemRepository itemRepository;
    @Override
    public List<Item> findAll() {
        return itemRepository.findAll();
    }

    @Override
    public Optional<Item> add(Item item) {
        Item result = itemRepository.save(item);
        if (Optional.of(result).isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result);
    }

    @Override
    public boolean update(Item item) {
        itemRepository.save(item);
        return itemRepository.findById(item.getId()).isPresent();
    }

    @Override
    public Optional<Item> findById(int id) {
        return itemRepository.findById(id);
    }

    @Override
    public boolean delete(Item item) {
        return itemRepository.findById(item.getId()).isEmpty();
    }

    @Override
    public Page<Item> findAllItemsByStatusAndUsers(Pageable pageable, Status status, List<User> users) {
        return itemRepository.findAllItemsByStatusAndUsersIn(pageable, status, users);
    }

    @Override
    public Page<Item> findAllItemsByStatus(Pageable pageable, Status status) {
        return itemRepository.findAllItemsByStatus(pageable, status);
    }

    @Override
    public List<Item> findAllItemContainsUser(User user) {
        return itemRepository.findAllItemsByUsersIn(List.of(user));
    }

    @Override
    public boolean itemContains(Item item, Status status, String userName) {
        var statusEqual = item.getStatus().equals(status);
        return userName != null ? statusEqual
                && (item.getUsers().stream().map(User::getUsername).
                anyMatch(s -> s.equals(userName))) : statusEqual;
    }

    @Override
    public Item addItemDto(ItemDto itemDto) {
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setItemText(itemDto.getItemText());
        return item;
    }

}
