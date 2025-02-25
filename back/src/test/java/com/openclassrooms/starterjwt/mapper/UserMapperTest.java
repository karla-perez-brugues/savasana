package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @Test
    public void testDtoListToEntityList() {
        List<UserDto> userDtoList = getUserDtoList();

        List<User> userList = userMapper.toEntity(userDtoList);

        assertNotNull(userList);
        assertEquals(userDtoList.size(), userList.size());

        assertEquals(1L, userList.get(0).getId());
        assertEquals("user-1@mail.com", userList.get(0).getEmail());
        assertFalse(userList.get(0).isAdmin());

        assertEquals(2L, userList.get(1).getId());
        assertEquals("user-2@mail.com", userList.get(1).getEmail());
        assertTrue(userList.get(1).isAdmin());

        assertEquals(3L, userList.get(2).getId());
        assertEquals("user-3@mail.com", userList.get(2).getEmail());
        assertFalse(userList.get(2).isAdmin());
    }

    @Test
    public void testEntityListToDtoList() {
        List<User> userList = getUserList();

        List<UserDto> userDtoList = userMapper.toDto(userList);

        assertNotNull(userDtoList);
        assertEquals(userDtoList.size(), userList.size());

        assertEquals(1L, userDtoList.get(0).getId());
        assertEquals("user-1@mail.com", userDtoList.get(0).getEmail());
        assertFalse(userDtoList.get(0).isAdmin());

        assertEquals(2L, userDtoList.get(1).getId());
        assertEquals("user-2@mail.com", userDtoList.get(1).getEmail());
        assertTrue(userDtoList.get(1).isAdmin());

        assertEquals(3L, userDtoList.get(2).getId());
        assertEquals("user-3@mail.com", userDtoList.get(2).getEmail());
        assertFalse(userDtoList.get(2).isAdmin());
    }

    @Test
    public void testDtoToEntity() {
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        userDto.setEmail("karla@mail.com");
        userDto.setLastName("Pérez");
        userDto.setFirstName("Karla");
        userDto.setAdmin(false);
        userDto.setPassword("password");

        User user = userMapper.toEntity(userDto);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("karla@mail.com", user.getEmail());
        assertEquals("Pérez", user.getLastName());
        assertEquals("Karla", user.getFirstName());
        assertFalse(user.isAdmin());
    }

    @Test
    public void testEntityToDto() {
        User user = new User()
                .setId(1L)
                .setEmail("karla@mail.com")
                .setLastName("Pérez")
                .setFirstName("Karla")
                .setAdmin(true)
                .setPassword("password");

        UserDto dto = userMapper.toDto(user);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("karla@mail.com", dto.getEmail());
        assertEquals("Pérez", dto.getLastName());
        assertEquals("Karla", dto.getFirstName());
        assertTrue(dto.isAdmin());
    }

    private static List<UserDto> getUserDtoList() {
        UserDto userDto1 = new UserDto();
        userDto1.setId(1L);
        userDto1.setEmail("user-1@mail.com");
        userDto1.setLastName("First Name 1");
        userDto1.setFirstName("Last Name 1");
        userDto1.setAdmin(false);
        userDto1.setPassword("password");

        UserDto userDto2 = new UserDto();
        userDto2.setId(2L);
        userDto2.setEmail("user-2@mail.com");
        userDto2.setLastName("First Name 2");
        userDto2.setFirstName("Last Name 2");
        userDto2.setAdmin(true);
        userDto2.setPassword("password");

        UserDto userDto3 = new UserDto();
        userDto3.setId(3L);
        userDto3.setEmail("user-3@mail.com");
        userDto3.setLastName("First Name 3");
        userDto3.setFirstName("Last Name 3");
        userDto3.setAdmin(false);
        userDto3.setPassword("password");

        return List.of(userDto1, userDto2, userDto3);
    }

    private static List<User> getUserList() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("user-1@mail.com");
        user1.setLastName("First Name 1");
        user1.setFirstName("Last Name 1");
        user1.setAdmin(false);
        user1.setPassword("password");

        User user2 = new User();
        user2.setId(2L);
        user2.setEmail("user-2@mail.com");
        user2.setLastName("First Name 2");
        user2.setFirstName("Last Name 2");
        user2.setAdmin(true);
        user2.setPassword("password");

        User user3 = new User();
        user3.setId(3L);
        user3.setEmail("user-3@mail.com");
        user3.setLastName("First Name 3");
        user3.setFirstName("Last Name 3");
        user3.setAdmin(false);
        user3.setPassword("password");

        return List.of(user1, user2, user3);
    }

}
