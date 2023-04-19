package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.ItemDto;

import java.util.Collection;

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
     * @param id
     * @return ItemDto or null if no object exists
     */
    ItemDto getItem(Long id);

    /**
     * Returns a collection of the owner's items by owner_id
     *
     * @param ownerId
     * @return Collection<ItemDto> or null if no object exists
     */
    Collection<ItemDto> getItems(Long ownerId);

    /**
     * Returns a collection of things by keyword text
     *
     * @param text
     * @return Collection<ItemDto> or null if no object exists
     */
    Collection<ItemDto> getItemByText(String text);
}