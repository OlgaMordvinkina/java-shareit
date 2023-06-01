package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestClient client;

    @PostMapping
    ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @Valid @RequestBody ItemRequestDto requestDto) {
        log.info("Получен запрос POST /requests");
        return client.createRequest(userId, requestDto);
    }

    @GetMapping
    ResponseEntity<Object> getRequestsOwn(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /requests");
        return client.getRequestsOwn(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getRequestsOtherUser(@RequestHeader("X-Sharer-User-Id") Long userId,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /requests/all");
        return client.getRequestsOtherUser(userId, from, size);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") Long userId,
                                      @PathVariable Long requestId) {
        log.info("Получен запрос GET /requests/{requestId}");
        return client.getRequest(userId, requestId);
    }
}
