package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.exceptions.AccessException;
import ru.practicum.shareit.exceptions.NotFoundItemException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundUserException(ownerId));
        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(owner);
        log.info("Добавлена Item: {}", item);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, Long ownerId) {
        User owner = userRepository.findById(ownerId).orElseThrow(() -> new NotFoundUserException(ownerId));
        Item oldItem = itemRepository.findById(itemDto.getId()).orElseThrow(() -> new NotFoundItemException(itemDto.getId()));
        if (!Objects.equals(owner.getId(), oldItem.getOwner().getId())) {
            throw new AccessException("Вы не являетесь владельцем Item.");
        }
        Item updateItem = ItemMapper.toItem(itemDto);
        updateItem.setOwner(owner);

        if (updateItem.getName() != null) {
            oldItem.setName(updateItem.getName());
        }
        if (updateItem.getDescription() != null) {
            oldItem.setDescription(updateItem.getDescription());
        }
        if (updateItem.getAvailable() != null) {
            oldItem.setAvailable(updateItem.getAvailable());
        }
        log.info("Обновлена Item: {}", oldItem);
        return ItemMapper.toItemDto(itemRepository.save(oldItem));
    }

    @Transactional(readOnly = true)
    @Override
    public ItemFullDto getItem(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
        if (!userRepository.existsById(userId)) throw new NotFoundUserException(userId);
        List<CommentDto> comments = commentRepository.findCommentsByItem_Id(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        log.info("Item c ID: {}", item);
        BookingShortDto start = null;
        BookingShortDto end = null;
        if (Objects.equals(item.getOwner().getId(), userId)) {
            start = findLast(itemId);
            end = findNext(itemId);
        }
        return ItemMapper.toItemFullDto(item, start, end, comments);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemFullDto> getItems(int from, int size, Long ownerId) {
        Pageable pages = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));
        List<ItemFullDto> items = itemRepository.findAllByOwnerId(ownerId, pages).stream()
                .map(it -> ItemMapper.toItemFullDto(it,
                                (Objects.equals(it.getOwner().getId(), ownerId) ? findLast(it.getId()) : null),
                                (Objects.equals(it.getOwner().getId(), ownerId) ? findNext(it.getId()) : null),
                                commentRepository.findCommentsByItem_Id(it.getId()).stream()
                                        .map(CommentMapper::toCommentDto)
                                        .collect(Collectors.toList())
                        )
                )
                .collect(Collectors.toList());
        log.info("Items в списке: {}", items.size());

        return items;
    }

    @Transactional(readOnly = true)
    @Override
    public List<ItemDto> getItemByText(int from, int size, String text) {
        List<ItemDto> items = new ArrayList<>();
        Pageable pages = PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id"));

        if (!Objects.equals(text, "")) {
            items = itemRepository.findAllByNameContainsIgnoreCaseOrDescriptionContainsIgnoreCaseAndAvailableIsTrue(text, text, pages).stream()
                    .map(ItemMapper::toItemDto)
                    .collect(Collectors.toList());
        }
        log.info("Items в списке: {}", items.size());
        return items;
    }

    @Override
    public CommentDto addComment(CommentDto commentDto, Long authorId, Long itemId) throws NotValidDataException {
        User author = userRepository.findById(authorId).orElseThrow(() -> new NotFoundUserException(authorId));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new NotFoundItemException(itemId));
        List<Booking> booking = bookingRepository.findBookingByItem_IdAndBooker_Id(itemId, authorId);
        if (booking.isEmpty()) {
            throw new NotValidDataException("Вы не можете оставить отзыв о вещи, которую не бронировали");
        }
        if (booking.stream().noneMatch(it -> it.getStart().isBefore(LocalDateTime.now()))) {
            throw new NotValidDataException("Бронирование ещё не наступило");
        }
        commentDto.setItem(item);
        commentDto.setAuthorName(author.getName());
        commentDto.setCreated(LocalDateTime.now());
        Comment comment = CommentMapper.toComment(commentDto, author);
        log.info("Добавлен Comment: {}", comment);
        return CommentMapper.toCommentDto(commentRepository.save(comment));
    }

    private BookingShortDto findNext(Long itemId) {
        Sort sortDescByStart = Sort.by(Sort.Direction.ASC, "start");
        Booking next = bookingRepository.findFirstByItem_IdAndStartAfter(itemId, LocalDateTime.now(), sortDescByStart);
        return (next != null && next.getStatus() == Status.APPROVED ? BookingMapper.toBookingShortDto(next) : null);
    }

    private BookingShortDto findLast(Long itemId) {
        Sort sortDescByStart = Sort.by(Sort.Direction.DESC, "start");
        Booking last = bookingRepository.findFirstByItem_IdAndStartBefore(itemId, LocalDateTime.now(), sortDescByStart);
        return (last != null && last.getStatus() == Status.APPROVED ? BookingMapper.toBookingShortDto(last) : null);
    }

}
