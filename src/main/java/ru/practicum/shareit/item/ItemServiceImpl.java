package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.UserStorage;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {
    private final ItemStorage storage;
    private final ItemMapper mapper;
    private final UserStorage userStorage;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        if (owner == null) {
            throw new NotFoundUserException(ownerId);
        }
        Item item = mapper.toItem(itemDto);
        item.setOwner(owner);
        log.info("Получен запрос POST /items");
        log.info("Добавлена Item: {}", item);
        return mapper.toItemDto(storage.createItem(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        User owner = userStorage.getUser(ownerId);
        Item item = storage.getItem(itemDto.getId());
        if (owner == null) {
            throw new NotFoundUserException(ownerId);
        }
        if (item == null) {
            throw new NotFoundItemException(itemDto.getId());
        }
        if (!Objects.equals(owner.getId(), item.getOwner().getId())) {
            throw new AccessException("Вы не являетесь владельцем Item.");
        }
        Item updateItem = mapper.toItem(itemDto);
        updateItem.setOwner(owner);
        log.info("Получен запрос PUT /items");
        log.info("Обновлена Item: {}", updateItem);
        return mapper.toItemDto(storage.updateItem(updateItem));
    }

    @Override
    public ItemDto getItem(Long id) {
        if (storage.getItem(id) == null) {
            throw new NotFoundItemException(id);
        }
        Item item = storage.getItem(id);
        log.info("Item c ID: {}", item);
        return mapper.toItemDto(item);
    }

    @Override
    public Collection<ItemDto> getItems(Long ownerId) {
        log.info("Получен запрос GET /items");
        Collection<ItemDto> items = storage.getItems(ownerId).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
        log.info("Items в списке: {}", items.size());
        return items;
    }

    @Override
    public Collection<ItemDto> getItemByText(String text) {
        Collection<ItemDto> items = new ArrayList<>();
        log.info("Получен запрос GET /items");
        if (!Objects.equals(text, "")) {
            items = storage.getItemByText(text.toLowerCase(Locale.ENGLISH)).stream()
                    .map(mapper::toItemDto)
                    .collect(Collectors.toList());
        }
        log.info("Items в списке: {}", items.size());
        return items;
    }
}
