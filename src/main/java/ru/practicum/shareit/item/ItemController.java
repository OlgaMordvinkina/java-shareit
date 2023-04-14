package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;
    private final ItemMapper mapper;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @NotNull(message = "Owner не может быть null")
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = mapper.toItem(itemDto, ownerId);
        return new ResponseEntity<>(mapper.toItemDto(service.createItem(item)), HttpStatus.OK);
    }

    @PatchMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long itemId,
                                              @RequestBody ItemDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Item item = mapper.toItem(itemDto, ownerId);
        item.setId(itemId);
        return new ResponseEntity<>(mapper.toItemDto(service.updateItem(item)), HttpStatus.OK);
    }

    @GetMapping(value = "/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {
        return new ResponseEntity<>(mapper.toItemDto(service.getItem(itemId)), HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        Collection<ItemDto> items = service.getItems(ownerId).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Collection<ItemDto>> getItemByText(@RequestParam String text) {
        Collection<ItemDto> items = service.getItemByText(text).stream()
                .map(mapper::toItemDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(items, HttpStatus.OK);
    }
}
