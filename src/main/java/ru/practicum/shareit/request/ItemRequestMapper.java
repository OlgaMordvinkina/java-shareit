package ru.practicum.shareit.request;

import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;

import java.util.List;

public class ItemRequestMapper {
    public static ItemRequestDto toItemRequestDto(ItemRequest request) {
        return new ItemRequestDto(
                request.getId(),
                request.getDescription(),
                request.getCreated()
        );
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, Long requesterId) {
        return new ItemRequest(
                requestDto.getId(),
                requestDto.getDescription(),
                requesterId,
                requestDto.getCreated()
        );
    }

    public static ItemRequestReplyDto toItemRequestReplyDto(ItemRequest request, List<ItemDto> items) {
        return new ItemRequestReplyDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items
        );
    }

    public static ItemRequestReplyDto toItemRequestReplyDto(ItemRequestDto request, List<ItemDto> items) {
        return new ItemRequestReplyDto(
                request.getId(),
                request.getDescription(),
                request.getCreated(),
                items
        );
    }
}
