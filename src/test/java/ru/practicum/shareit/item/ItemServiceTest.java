package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.exceptions.NotValidDataException;
import ru.practicum.shareit.item.comments.Comment;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.comments.CommentMapper;
import ru.practicum.shareit.item.comments.CommentRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.item.model.ItemFullDto;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;
    private ItemService itemService;
    private final Long id = 1L;
    private final ItemDto itemDto = new ItemDto(1L, "name", "description", true, 1L);
    private final Item item = ItemMapper.toItem(itemDto);
    private final LocalDateTime now = LocalDateTime.parse(LocalDate.now() + "T09:55:05.000001");
    private final CommentDto commentDto = new CommentDto(id, "text", item, "name", now);
    private final User user = new User(id, "name", "email@mail.ru");

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, userRepository, commentRepository, bookingRepository);
    }

    @Test
    void createItem_itemFound_thenReturnedItem() {
        Item item = ItemMapper.toItem(itemDto);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);

        ItemDto expectedUser = itemService.createItem(itemDto, id);

        assertEquals(itemDto, expectedUser);
        verify(itemRepository).save(any());
    }

    @Test
    void updateItem_itemFound_thenReturnedItem() {
        item.setName("updateName");
        item.setDescription("updateDescription");

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User()));
        when(itemRepository.save(any())).thenReturn(item);
        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));

        ItemDto expectedItem = itemService.updateItem(itemDto, id);

        assertEquals(itemDto, expectedItem);
        verify(itemRepository).save(any());
    }

    @Test
    void updateItem_itemFound_thenReturnedAccessException() {
        Item item = ItemMapper.toItem(itemDto);

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));

        assertThrows(AccessException.class, () -> itemService.updateItem(itemDto, id));
    }

    @Test
    void getItemById_itemFound_thenReturnedItem() {
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.existsById(anyLong())).thenReturn(true);

        ItemDto expectedItem = ItemMapper.toItemDto(itemService.getItem(item.getId(), id));
        expectedItem.setRequestId(id);

        assertEquals(expectedItem, itemDto);
    }

    @Test
    void getItemById_itemNotFound_thenReturnedNotFoundItemException() {
        when(itemRepository.findById(anyLong())).thenThrow(new NotFoundItemException(id));

        assertThrows(NotFoundItemException.class, () -> itemService.getItem(id, id));
    }

    @Test
    void getItems_listItemDto() {
        ArrayList<Item> items = new ArrayList<>();
        items.add(item);

        when(itemRepository.findAllByOwnerId(anyLong(), any(Pageable.class))).thenReturn(items);
        when(commentRepository.findCommentsByItem_Id(anyLong())).thenReturn(new ArrayList<>());
        List<ItemFullDto> expectedItems = itemService.getItems(0, 10, id);

        assertNotNull(expectedItems);
    }

    @Test
    void getItemByText_listItemDto() {
        List<Item> listDto = new ArrayList<>();

        when(itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(
                anyString(),
                anyString(),
                any(Pageable.class))
        ).thenReturn(listDto);

        List<ItemDto> expectedItems = itemService.getItemByText(0, 10, "text");

        assertNotNull(expectedItems);
        assertTrue(expectedItems.isEmpty());
    }

    @Test
    void addComment_commentFound_thenReturnedComment() throws NotValidDataException {
        item.setAvailable(true);
        User user = new User();
        CommentDto commentDto = new CommentDto(id, "text", item, "name", now);
        Comment comment = CommentMapper.toComment(commentDto, user);
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(id, now.minusDays(5), now.minusDays(2), item, user, Status.APPROVED));

        when(itemRepository.findById(anyLong())).thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(bookingRepository.findBookingByItem_IdAndBooker_Id(anyLong(), anyLong())).thenReturn(bookings);
        when(commentRepository.save(any())).thenReturn(comment);

        CommentDto expectedComment = itemService.addComment(commentDto, id, id);
        commentDto.setCreated(now);
        assertEquals(commentDto, expectedComment);
        verify(commentRepository).save(any());
    }

    @Test
    void addComment_bookingIsEmpty_thenReturnedNotValidDataException() {
        ArrayList<Booking> bookings = new ArrayList<>();

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(id, "name", "email@mail.ru")));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItem_IdAndBooker_Id(anyLong(), anyLong())).thenReturn(bookings);

        assertThrows(NotValidDataException.class, () -> itemService.addComment(commentDto, id, id));
    }

    @Test
    void addComment_bookingNot_thenReturnedNotValidDataException() {
        Item item = ItemMapper.toItem(itemDto);
        ArrayList<Booking> bookings = new ArrayList<>();
        bookings.add(new Booking(id, now.plusDays(3), now.plusDays(4), new Item(), new User(), Status.APPROVED));

        when(userRepository.findById(anyLong())).thenReturn(Optional.of(new User(id, "name", "email@mail.ru")));
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(bookingRepository.findBookingByItem_IdAndBooker_Id(anyLong(), anyLong())).thenReturn(bookings);

        assertThrows(NotValidDataException.class, () -> itemService.addComment(commentDto, id, id));
    }
}
