package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemStorage {

    /**
     * Create a new Item
     *
     * @param item
     * @return new Item
     */
    Item createItem(Item item);

    /**
     * Updates the film
     * If the item is not found throws NotFoundItemException
     *
     * @param item
     * @return updated Item
     */
    Item updateItem(Item item);

    /**
     * Returns a Item by id
     * If the item is not found throws NotFoundItemException
     *
     * @param id
     * @return Item or null if no object exists
     */
    Item getItem(Long id);

    /**
     * Returns a Item by id
     *
     * @param ownerId
     * @return Collection<Item> or null if no object exists
     */
    Collection<Item> getItems(Long ownerId);

    /**
     * Returns a Collection<User> by contains text
     *
     * @param text
     * @return Collection<Item> or null if no object exists
     */
    Collection<Item> getItemByText(String text);
}
