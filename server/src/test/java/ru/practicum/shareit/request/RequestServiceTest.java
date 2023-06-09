package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RequestServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    private ItemRequestService service;
    private final Long id = 1L;
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final ItemRequestDto requestDto = new ItemRequestDto(id, "name", now);
    private final ItemRequest request = ItemRequestMapper.toItemRequest(requestDto, id);
    private final ItemRequestReplyDto requestReplyDto = ItemRequestMapper.toItemRequestReplyDto(requestDto, new ArrayList<>());

    @BeforeEach
    void beforeEach() {
        service = new ItemRequestServiceImpl(itemRepository, userRepository, requestRepository);
    }

    @Test
    void createRequest_RequestFound_thenReturnedItemRequestDto() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.save(any())).thenReturn(request);

        ItemRequestDto expectedUser = service.createRequest(requestDto, id);
        requestDto.setCreated(now);

        assertEquals(requestDto, expectedUser);
        verify(requestRepository).save(any());
    }

    @Test
    void getRequestsOwn_ListItemRequestReplyDto() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.findItemRequestsByRequesterId(anyLong(), any(Sort.class))).thenReturn(new ArrayList<>());

        List<ItemRequestReplyDto> expectedRequest = service.getRequestsOwn(id);

        assertNotNull(expectedRequest);
        assertTrue(expectedRequest.isEmpty());
    }

    @Test
    void getRequestsOtherUser_ListItemRequestReplyDto() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.findItemRequestsByRequesterIdNotOrderByCreatedDesc(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(Collections.singletonList(request)));

        List<ItemRequestReplyDto> expectedItems = service.getRequestsOtherUser(0, 10, id);

        assertNotNull(expectedItems);
        assertFalse(expectedItems.isEmpty());
    }

    @Test
    void getRequest_ItemRequestReplyDto() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        when(requestRepository.findById(anyLong())).thenReturn(Optional.of(request));

        ItemRequestReplyDto expectedItem = service.getRequest(id, id);

        assertEquals(expectedItem, requestReplyDto);
    }
}
