package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable()
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                new User(),
                itemDto.getAvailable()
        );
    }

    public static ItemFullDto toItemFullDto(Item item, BookingShortDto last, BookingShortDto next, List<CommentDto> comments) {
        return new ItemFullDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                last,
                next,
                comments
        );
    }
}
