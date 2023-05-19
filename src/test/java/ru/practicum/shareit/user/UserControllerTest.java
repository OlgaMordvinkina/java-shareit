package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.model.UserDto;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserControllerTest {
    @MockBean
    private UserService service;
    private final ObjectMapper mapper;
    private final MockMvc mvc;
    private final UserDto userOne = new UserDto(1L, "nameUser", "email@user.ru");
    private final UserDto userTwo = new UserDto(1L, "nameUser", "email@user.ru");

    @Test
    void createUserEmptyNameTest() throws Exception {
        UserDto user = new UserDto(3L, "", "email@user.ru");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserEmptyEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserNotValidEmailTest() throws Exception {
        UserDto user = new UserDto(3L, "name", "emailUser.ru");
        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUserTest() throws Exception {
        when(service.createUser(any())).thenReturn(userOne);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("nameUser"))
                .andExpect(jsonPath("$.email").value("email@user.ru"));
    }

    @Test
    void updateUserTest() throws Exception {
        userOne.setName("updateName");
        userOne.setEmail("update@email.ru");

        when(service.updateUser(userOne)).thenReturn(userOne);

        mvc.perform(patch("/users/1")
                        .content(mapper.writeValueAsString(userOne))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("updateName"))
                .andExpect(jsonPath("$.email").value("update@email.ru"));
    }

    @Test
    void getUserByIdTest() throws Exception {
        when(service.getUser(anyLong())).thenReturn(userOne);

        mvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("nameUser"))
                .andExpect(jsonPath("$.email").value("email@user.ru"));
    }

    @Test
    void getAllUsersTest() throws Exception {
        List<UserDto> listDto = new ArrayList<>();
        listDto.add(userOne);
        listDto.add(userTwo);

        when(service.getUsers()).thenReturn(listDto);

        mvc.perform(get("/users")
                        .content(mapper.writeValueAsString(listDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
    }

    @Test
    void deleteUserTest() throws Exception {
        mvc.perform(delete("/users/1"))
                .andExpect(status().isOk());
    }
}
