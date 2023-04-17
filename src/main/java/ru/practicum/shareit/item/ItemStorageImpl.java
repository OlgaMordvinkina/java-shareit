package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 0L;

    @Override
    public Item createItem(Item item) {
        item.setId(++itemId);
        items.put(item.getId(), item);
        return item;
    }

    @Override
    public Item updateItem(Item item) {
        if (!items.containsKey(item.getId())) {
            throw new NotFoundItemException(item.getId());
        }
        if (!Objects.equals(item.getOwner(), items.get(item.getId()).getOwner())) {
            throw new AccessException("Вы не являетесь владельцем Item.");
        }
        Item newItem = items.get(item.getId());
        if (item.getName() != null) {
            newItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            newItem.setDescription(item.getDescription());
        }
        if (item.getOwner() != null) {
            newItem.setOwner(item.getOwner());
        }
        if (item.getAvailable() != null) {
            newItem.setAvailable(item.getAvailable());
        }
        items.put(item.getId(), newItem);
        return newItem;
    }

    @Override
    public Item getItem(Long id) {
        if (!items.containsKey(id)) {
            throw new NotFoundItemException(id);
        }
        return items.get(id);
    }

    @Override
    public Collection<Item> getItems(Long ownerId) {
        return items.values().stream().filter(it -> Objects.equals(it.getOwner(), ownerId)).collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getItemByText(String text) {
        Collection<Item> itemsSearch = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getAvailable()
                    && !Objects.equals(text, "")
                    && (item.getName().toLowerCase().contains(text)
                    || item.getDescription().toLowerCase().contains(text))) {
                itemsSearch.add(item);
            }
        }
        return itemsSearch;
    }
}
