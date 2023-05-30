package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final String header = "X-Sharer-User-Id";
    private final ItemRequestDto requestOne = new ItemRequestDto(1L, "description", LocalDateTime.now());
    private final ItemRequestDto requestTwo = new ItemRequestDto(1L, "description", LocalDateTime.now());

    @Test
    void createRequestTest() throws Exception {
        when(service.createRequest(any(), anyLong())).thenReturn(requestOne);

        mvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(header, 1L)
                        .content(mapper.writeValueAsString(requestOne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.created").hasJsonPath());
    }

    @Test
    void getRequestsOwnTest() throws Exception {
        List<ItemRequestDto> listDto = new ArrayList<>();
        listDto.add(requestOne);
        listDto.add(requestTwo);

        when(service.getRequestsOwn(anyLong())).thenReturn(
                listDto.stream()
                        .map(it -> ItemRequestMapper.toItemRequestReplyDto(it, null))
                        .collect(Collectors.toList())
        );

        mvc.perform(get("/requests")
                        .content(mapper.writeValueAsString(listDto))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getRequestsOtherUserTest() throws Exception {
        List<ItemRequestDto> listDto = new ArrayList<>();
        listDto.add(requestOne);
        listDto.add(requestTwo);

        when(service.getRequestsOtherUser(anyInt(), anyInt(), anyLong())).thenReturn(
                listDto.stream()
                        .map(it -> ItemRequestMapper.toItemRequestReplyDto(it, null))
                        .collect(Collectors.toList())
        );

        mvc.perform(get("/requests/all")
                        .content(mapper.writeValueAsString(listDto))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getRequestTest() throws Exception {
        when(service.getRequest(anyLong(), anyLong())).thenReturn(ItemRequestMapper.toItemRequestReplyDto(requestOne, null));

        mvc.perform(get("/requests/1")
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value("description"));
    }
}
