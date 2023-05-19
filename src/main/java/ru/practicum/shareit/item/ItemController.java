package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotValidDataException;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @NotNull(message = "Owner не может быть null")
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос POST /items");
        return service.createItem(itemDto, ownerId);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Получен запрос PATCH /items/{itemId}");
        itemDto.setId(itemId);
        return service.updateItem(itemDto, ownerId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemFullDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Получен запрос GET /items/{itemId}");
        return service.getItem(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemFullDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                            @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                            @RequestParam(defaultValue = "10") @Min(value = 1) int size) {
        log.info("Получен запрос GET /items");
        return service.getItems(from, size, ownerId);
    }

    @GetMapping(value = "/search")
    public Collection<ItemDto> getItemByText(@RequestParam String text,
                                             @RequestParam(defaultValue = "0") @Min(value = 0) int from,
                                             @RequestParam(defaultValue = "10") @Min(value = 1) int size) {
        log.info("Получен запрос GET /items/search");
        return service.getItemByText(from, size, text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) throws NotValidDataException {
        log.info("Получен запрос POST /items/{itemId}/comment");
        return service.addComment(commentDto, userId, itemId);
    }
}
