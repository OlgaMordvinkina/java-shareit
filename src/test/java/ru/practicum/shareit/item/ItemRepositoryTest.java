package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRepositoryTest {
    @Mock
    private ItemRepository itemRepository;
    private final Item item = new Item(1L, "name", "description", new User(), true, 1L);
    private final ArrayList<Item> items = new ArrayList<>(Collections.singletonList(item));

    @Test
    void createItem_itemFound_thenReturnedItem() {
        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class)))
                .thenReturn(items);

        assertEquals(items, itemRepository.findAllByOwnerId(1L, Pageable.unpaged()));
    }

    @Test
    void updateItem_itemFound_thenReturnedItem() {
        when(itemRepository.findAllByRequestIdOrderByIdDesc(anyLong()))
                .thenReturn(items);

        assertEquals(items, itemRepository.findAllByRequestIdOrderByIdDesc(1L));
    }

    @Test
    void updateItem_itemFound_thenReturnedAccessException() {
        when(itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(
                anyString(),
                anyString(),
                any(Pageable.class)
        ))
                .thenReturn(items);

        assertEquals(items, itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(
                "name",
                "description",
                Pageable.unpaged()
        ));
    }
}
