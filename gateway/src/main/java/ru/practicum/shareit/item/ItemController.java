package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemClient client;

    @PostMapping()
    public ResponseEntity<Object> createItem(@NotNull(message = "Owner не может быть null")
                                             @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @Valid @RequestBody ItemDto itemDto) {
        log.info("Получен запрос POST /items");
        return client.createItem(ownerId, itemDto);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable Long itemId,
                                             @RequestBody ItemDto itemDto) {
        log.info("Получен запрос PATCH /items/{itemId}");
        return client.updateItem(ownerId, itemId, itemDto);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader("X-Sharer-User-Id") Long userId,
                                          @PathVariable Long itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return client.getItem(userId, itemId);
    }

    @GetMapping()
    public ResponseEntity<Object> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                           @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                           @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /items");
        return client.getItems(from, size, ownerId);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Object> getItemByText(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                                @RequestParam String text,
                                                @RequestParam(defaultValue = "0") @PositiveOrZero int from,
                                                @RequestParam(defaultValue = "10") @Positive int size) {
        log.info("Получен запрос GET /items/search");
        return client.getItemByText(ownerId, from, size, text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public ResponseEntity<Object> addComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @PathVariable Long itemId,
                                             @RequestBody CommentDto commentDto) {
        log.info("Получен запрос POST /items/{itemId}/comment");
        return client.addComment(userId, itemId, commentDto);
    }
}
