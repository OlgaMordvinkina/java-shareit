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
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemControllerTest {
    @MockBean
    private ItemClient client;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final String header = "X-Sharer-User-Id";

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
                        .header(header, 1L)
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
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createItemEmptyEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "");
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(user))
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserNotValidEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "emailUser.ru");
        mvc.perform(post("/items")
                        .content(mapper.writeValueAsString(user))
                        .header(header, 1L)
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
                        .header(header, 1L)
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
                        .header(header, 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

}
