package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemService {
    /**
     * Create a new Item
     *
     * @param item
     * @return new Item
     */
    Item createItem(Item item);

    /**
     * Updates the film
     *
     * @param item
     * @return updated Item
     */
    Item updateItem(Item item);

    /**
     * Returns a Item by id
     *
     * @param id
     * @return Item or null if no object exists
     */
    Item getItem(Long id);

    /**
     * Returns a collection of the owner's items by owner_id
     *
     * @param ownerId
     * @return Item or null if no object exists
     */
    Collection<Item> getItems(Long ownerId);

    /**
     * Returns a collection of things by keyword text
     *
     * @param text
     * @return Item or null if no object exists
     */
    Collection<Item> getItemByText(String text);
}