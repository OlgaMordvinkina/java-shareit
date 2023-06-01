package ru.practicum.shareit.item;

import ru.practicum.shareit.exceptions.NotValidDataException;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;

import java.util.List;

public interface ItemService {
    /**
     * Create a new Item
     *
     * @param itemDto
     * @param ownerId
     * @return new ItemDto
     */
    ItemDto createItem(ItemDto itemDto, Long ownerId);

    /**
     * Updates the Item
     *
     * @param itemDto
     * @param ownerId
     * @return updated ItemDto
     */
    ItemDto updateItem(ItemDto itemDto, Long ownerId);

    /**
     * Returns a ItemDto by id
     *
     * @param itemId
     * @param userId
     * @return ItemDto or null if no object exists
     */
    ItemFullDto getItem(Long itemId, Long userId);

    /**
     * Returns a collection of the owner's items by owner_id
     *
     * @param ownerId
     * @return Collection<ItemFullDto> or null if no object exists
     */
    List<ItemFullDto> getItems(int from, int size, Long ownerId);

    /**
     * Returns a collection of things by keyword text
     *
     * @param text
     * @return Collection<ItemDto> or null if no object exists
     */
    List<ItemDto> getItemByText(int from, int size, String text);

    /**
     * Add comment
     *
     * @param commentDto
     * @param authorId
     * @param itemId
     * @return an CommentDto or AccessException
     */
    CommentDto addComment(CommentDto commentDto, Long authorId, Long itemId) throws NotValidDataException;
}