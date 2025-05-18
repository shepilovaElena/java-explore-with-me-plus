package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.user.UserDto;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserServiceImpl userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(UserDto userDto) {
        return userService.createUser(userDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam List<Long> usersIdList,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        return userService.getUsers(usersIdList,from, size);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable Long userId) {

    }
}
