package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;

import java.util.List;

public interface ItemRequestService {
    /**
     * @param requestDto
     * @param requesterId
     * @return
     */
    ItemRequestDto createRequest(ItemRequestDto requestDto, Long requesterId);

    /**
     * @param userId
     * @return
     */
    List<ItemRequestReplyDto> getRequestsOwn(Long userId);

    /**
     * @param from
     * @param size
     * @param userId
     * @return
     */
    List<ItemRequestReplyDto> getRequestsOtherUser(int from, int size, Long userId);

    /**
     * @param requestId
     * @param userId
     * @return
     */
    ItemRequestReplyDto getRequest(Long requestId, Long userId);

}
