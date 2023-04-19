package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
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
        return items.get(id);
    }

    @Override
    public Collection<Item> getItems(Long ownerId) {
        return items.values().stream()
                .filter(it -> Objects.equals(it.getOwner().getId(), ownerId))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Item> getItemByText(String text) {
        return items.values().stream()
                .filter(Item::getAvailable)
                .filter(it -> it.getName().toLowerCase(Locale.ENGLISH).contains(text) ||
                        it.getDescription().toLowerCase(Locale.ENGLISH).contains(text))
                .collect(Collectors.toList());
    }
}
