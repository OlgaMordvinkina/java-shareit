package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService service;

    @PostMapping()
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemDto itemDto,
                                              @NotNull(message = "Owner не может быть null")
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return new ResponseEntity<>(service.createItem(itemDto, ownerId), HttpStatus.OK);
    }

    @PatchMapping(value = "/{itemId}")
    public ResponseEntity<ItemDto> updateItem(@PathVariable Long itemId,
                                              @RequestBody ItemDto itemDto,
                                              @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        itemDto.setId(itemId);
        return new ResponseEntity<>(service.updateItem(itemDto, ownerId), HttpStatus.OK);
    }

    @GetMapping(value = "/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {
        return new ResponseEntity<>(service.getItem(itemId), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<Collection<ItemDto>> getItems(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return new ResponseEntity<>(service.getItems(ownerId), HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<Collection<ItemDto>> getItemByText(@RequestParam String text) {
        return new ResponseEntity<>(service.getItemByText(text), HttpStatus.OK);
    }
}
