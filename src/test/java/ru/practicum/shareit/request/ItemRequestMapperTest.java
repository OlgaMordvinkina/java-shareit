package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemRequestMapperTest {
    private final Long requesterId = 1L;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final ItemRequest request = new ItemRequest(1L, "description", requesterId, now);
    private final ItemRequestDto requestDto = new ItemRequestDto(1L, "description", now);
    private final ItemRequestReplyDto requestReplyDto = new ItemRequestReplyDto(1L, "description", now, new ArrayList<>());


    @Test
    void toItemRequestDto_FromItemRequest_returnedItemRequestDto() {
        ItemRequestDto expectedRequestDto = ItemRequestMapper.toItemRequestDto(request);

        assertEquals(requestDto, expectedRequestDto);
    }

    @Test
    void toItemRequest_FromItemRequestDto_returnedItemRequest() {
        ItemRequest expectedRequest = ItemRequestMapper.toItemRequest(requestDto, requesterId);

        assertEquals(request.toString(), expectedRequest.toString());
    }

    @Test
    void itemRequestReplyDto_FromItemRequest_returnedItemRequestReplyDto() {
        ItemRequestReplyDto expectedRequestReplyDto = ItemRequestMapper.toItemRequestReplyDto(request, new ArrayList<>());

        assertEquals(requestReplyDto, expectedRequestReplyDto);
    }

    @Test
    void itemRequestReplyDto_FromItemRequestDto_returnedItemRequestReplyDto() {
        ItemRequestReplyDto expectedRequestReplyDto = ItemRequestMapper.toItemRequestReplyDto(requestDto, new ArrayList<>());

        assertEquals(requestReplyDto, expectedRequestReplyDto);
    }
}
