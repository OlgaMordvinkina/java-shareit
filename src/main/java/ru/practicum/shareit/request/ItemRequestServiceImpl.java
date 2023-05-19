package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exceptions.NotFoundItemRequestException;
import ru.practicum.shareit.exceptions.NotFoundUserException;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestReplyDto;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final ItemRequestRepository requestRepository;

    @Override
    @Transactional
    public ItemRequestDto createRequest(ItemRequestDto requestDto) {
        userExists(requestDto.getRequesterId());
        requestDto.setCreated(LocalDateTime.now());
        ItemRequest request = ItemRequestMapper.toItemRequest(requestDto);
        log.info("Создан ItemRequest: {}", request);
        return ItemRequestMapper.toItemRequestDto(requestRepository.save(request));
    }

    @Override
    public List<ItemRequestReplyDto> getRequestsOwn(Long userId) {
        userExists(userId);

        Sort sortDescByCreated = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequestReplyDto> requests = requestRepository.findItemRequestsByRequesterId(userId, sortDescByCreated).stream()
                .map(it -> ItemRequestMapper.itemRequestReplyDto(it, getItemByRequestId(it.getRequesterId())))
                .collect(Collectors.toList());

        log.info("ItemRequest в списке {}", requests.size());
        return requests;
    }

    @Override
    public List<ItemRequestReplyDto> getRequestsOtherUser(int from, int size, Long userId) {
        userExists(userId);

        Pageable pages = PageRequest.of(from / size, size);
        List<ItemRequestReplyDto> requests = requestRepository.findItemRequestsByRequesterIdNotOrderByCreatedDesc(userId, pages).stream()
                .map(it -> ItemRequestMapper.itemRequestReplyDto(it, getItemByRequestId(it.getRequesterId())))
                .collect(Collectors.toList());

        log.info("ItemRequest в списке {}", requests.size());
        return requests;
    }

    @Override
    public ItemRequestReplyDto getRequest(Long requestId, Long userId) {
        userExists(userId);
        ItemRequest request = requestRepository.findById(requestId).orElseThrow(() -> new NotFoundItemRequestException(requestId));
        log.info("Получен ItemRequest {}", request);
        return ItemRequestMapper.itemRequestReplyDto(request, getItemByRequestId(request.getRequesterId()));
    }

    private List<ItemDto> getItemByRequestId(Long userId) {
        return itemRepository.findAllByRequestIdOrderByIdDesc(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void userExists(Long requestDto) {
        if (!userRepository.existsById(requestDto)) {
            throw new NotFoundUserException(requestDto);
        }
    }

}
