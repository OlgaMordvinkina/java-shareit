package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemMapperTest {
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final User user = new User(1L, "name", "email@mail.ru");
    private final Item item = new Item(1L, "name", "description", user, true, 1L);
    private final BookingShortDto booking = new BookingShortDto(1L, 1L, now, now);
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L);
    private final ItemFullDto itemFullDto = new ItemFullDto(1L, "name", "description", true, booking, booking, new ArrayList<>());

    @Test
    void toItemDtoFromItem_returnedItem() {
        ItemDto expectedItemDto = ItemMapper.toItemDto(item);

        assertEquals(itemDto, expectedItemDto);
    }

    @Test
    void toItemDtoFromItemFullDto_returnedItemFullDto() {
        ItemDto expectedItemDto = ItemMapper.toItemDto(itemFullDto);
        expectedItemDto.setRequestId(1L);

        assertEquals(itemDto, expectedItemDto);
    }

    @Test
    void toItemFromItemDto_returnedItemDto() {
        Item expectedItem = ItemMapper.toItem(itemDto);
        expectedItem.setOwner(user);

        assertEquals(item.toString(), expectedItem.toString());
    }

    @Test
    void toItemFullDtoFromItem_returnedItemFullDto() {
        ItemFullDto expectedItemFullDto = ItemMapper.toItemFullDto(item, booking, booking, new ArrayList<>());

        assertEquals(itemFullDto, expectedItemFullDto);
    }

    @Test
    void toItemFullDtoFromItemDto_returnedItemFullDto() {
        ItemFullDto expectedItemFullDto = ItemMapper.toItemFullDto(itemDto, booking, booking, new ArrayList<>());

        assertEquals(itemFullDto, expectedItemFullDto);
    }
}
