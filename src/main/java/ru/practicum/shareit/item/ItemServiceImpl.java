package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final UserService service;

    @Override
    public Item createItem(Item item) {
        service.getUser(item.getOwner());
        log.info("Получен запрос POST /items");
        log.info("Добавлена Item: {}", item);
        return storage.createItem(item);
    }

    @Override
    public Item updateItem(Item item) {
        service.getUser(item.getOwner());
        log.info("Получен запрос PUT /items");
        log.info("Обновлена Item: {}", item);
        return storage.updateItem(item);
    }

    @Override
    public Item getItem(Long id) {
        Item item = storage.getItem(id);
        log.info("Item c ID: {}", item);
        return item;
    }

    @Override
    public Collection<Item> getItems(Long ownerId) {
        log.info("Получен запрос GET /items");
        Collection<Item> items = storage.getItems(ownerId);
        log.info("Items в списке: {}", items.size());
        return items;
    }

    @Override
    public Collection<Item> getItemByText(String text) {
        log.info("Получен запрос GET /items");
        Collection<Item> items = storage.getItemByText(text.toLowerCase());
        log.info("Items в списке: {}", items.size());
        return items;
    }
}
