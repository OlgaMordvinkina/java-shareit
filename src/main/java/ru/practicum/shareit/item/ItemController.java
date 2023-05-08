package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.NotValidDataException;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ItemDto createItem(@Valid @RequestBody ItemDto itemDto,
                              @NotNull(message = "Owner не может быть null")
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return service.createItem(itemDto, ownerId);
    }

    @PatchMapping(value = "/{itemId}")
    public ItemDto updateItem(@PathVariable Long itemId,
                              @RequestBody ItemDto itemDto,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemDto.setId(itemId);
        return service.updateItem(itemDto, ownerId);
    }

    @GetMapping(value = "/{itemId}")
    public ItemFullDto getItem(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return service.getItem(itemId, userId);
    }

    @GetMapping()
    public Collection<ItemFullDto> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return service.getItems(ownerId);
    }

    @GetMapping(value = "/search")
    public Collection<ItemDto> getItemByText(@RequestParam String text) {
        return service.getItemByText(text);
    }

    @PostMapping(value = "/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto,
                                 @RequestHeader("X-Sharer-User-Id") Long userId) throws NotValidDataException {
        return service.addComment(commentDto, userId, itemId);
    }
}
