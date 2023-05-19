package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comments.CommentDto;
import ru.practicum.shareit.item.model.ItemDto;
import ru.practicum.shareit.user.model.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final String HEADER = "X-Sharer-User-Id";
    private final ItemDto itemOne = new ItemDto(1L, "name", "description", true, 1L);
    private final ItemDto itemTwo = new ItemDto(2L, "name", "description", true, 2L);

    @Test
    void createItemEmptyNameTest() throws Exception {
        ItemDto item = new ItemDto(
                1L,
                "",
                "description",
                true,
                1L
        );
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemNameMoreMaxTest() throws Exception {
        ItemDto item = new ItemDto(
                1L,
                Strings.repeat("name", 25),
                "description",
                true,
                1L
        );
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemEmptyEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "");
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(user))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserNotValidEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "emailUser.ru");
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(user))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemEmptyDescriptionTest() throws Exception {
        ItemDto item = new ItemDto(
                1L,
                "name",
                "",
                true,
                1L
        );
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemEmptyAvailableTest() throws Exception {
        ItemDto item = new ItemDto(
                1L,
                "name",
                "description",
                null,
                1L
        );
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(item))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemTest() throws Exception {
        when(service.createItem(any(), anyLong())).thenReturn(itemOne);

        mvc.perform(post("/items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(itemOne)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.requestId").value(1L));
    }

    @Test
    void updateItemTest() throws Exception {
        itemOne.setName("updateName");
        itemOne.setDescription("updateDescription");

        when(service.updateItem(any(), anyLong())).thenReturn(itemOne);

        mvc.perform(patch("/items/1")
                        .content(mapper.writeValueAsString(itemOne))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("updateName"))
                .andExpect(jsonPath("$.description").value("updateDescription"))
                .andExpect(jsonPath("$.available").value(true))
                .andExpect(jsonPath("$.requestId").value(1L));
    }

    @Test
    void getItemByIdTest() throws Exception {
        when(service.getItem(anyLong(), anyLong())).thenReturn(ItemMapper.toItemFullDto(itemOne, null, null, null));

        mvc.perform(get("/items/1")
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("name"))
                .andExpect(jsonPath("$.description").value("description"))
                .andExpect(jsonPath("$.available").value(true));
    }

    @Test
    void getAllItemsTest() throws Exception {
        List<ItemDto> listDto = new ArrayList<>();
        listDto.add(itemOne);
        listDto.add(itemTwo);

        when(service.getItems(anyInt(), anyInt(), anyLong())).thenReturn(
                listDto.stream()
                        .map(it -> ItemMapper.toItemFullDto(it, null, null, null))
                        .collect(Collectors.toList())
        );

        mvc.perform(get("/items")
                        .content(mapper.writeValueAsString(listDto))
                        .header(HEADER, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void getItemByTextTest() throws Exception {
        List<ItemDto> listDto = new ArrayList<>();
        listDto.add(itemOne);
        listDto.add(itemTwo);

        when(service.getItemByText(anyInt(), anyInt(), anyString())).thenReturn(listDto);

        mvc.perform(get("/items/search?text=descr")
                        .content(mapper.writeValueAsString(listDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void addCommentTest() throws Exception {
        CommentDto comment = new CommentDto(1L, "text", ItemMapper.toItem(itemOne), "authorName", LocalDateTime.now());
        when(service.addComment(any(), anyLong(), anyLong())).thenReturn(comment);

        mvc.perform(post("/items/1/comment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .header(HEADER, 1L)
                        .content(mapper.writeValueAsString(comment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.text").value("text"))
                .andExpect(jsonPath("$.authorName").value("authorName"));
    }

}
