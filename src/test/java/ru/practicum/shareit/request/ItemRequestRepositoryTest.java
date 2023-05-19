package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemRequestRepositoryTest {
    @Mock
    private ItemRequestRepository requestRepository;
    private final ItemRequest request = new ItemRequest(1L, "text", 2L, LocalDateTime.now());

    @Test
    void createItem_itemFound_thenReturnedItem() {
        ArrayList<ItemRequest> requests = new ArrayList<>(Collections.singletonList(request));
        when(requestRepository.findItemRequestsByRequesterId(anyLong(), any(Sort.class)))
                .thenReturn(requests);

        assertEquals(requests, requestRepository.findItemRequestsByRequesterId(1L, Sort.unsorted()));
    }

    @Test
    void updateItem_itemFound_thenReturnedItem() {
        Page<ItemRequest> requests = new PageImpl<>(Collections.singletonList(request));
        when(requestRepository.findItemRequestsByRequesterIdNotOrderByCreatedDesc(anyLong(), any(Pageable.class)))
                .thenReturn(requests);

        assertEquals(requests, requestRepository.findItemRequestsByRequesterIdNotOrderByCreatedDesc(
                1L,
                Pageable.unpaged()
        ));
    }
}
