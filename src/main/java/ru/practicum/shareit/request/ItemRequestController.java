package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDto createRequest(@Valid @RequestBody ItemRequestDto requestDto,
                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос POST /requests");
        return service.createRequest(requestDto, userId);
    }

    @GetMapping
    List<ItemRequestReplyDto> getRequestsOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /requests");
        return service.getRequestsOwn(userId);
    }

    @GetMapping("/all")
    List<ItemRequestReplyDto> getRequestsOtherUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                   @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                   @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /requests/all");
        return service.getRequestsOtherUser(from, size, userId);
    }

    @GetMapping("/{requestId}")
    ItemRequestReplyDto getRequest(@PathVariable Long requestId,
                                   @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /requests/{requestId}");
        return service.getRequest(requestId, userId);
    }
}
