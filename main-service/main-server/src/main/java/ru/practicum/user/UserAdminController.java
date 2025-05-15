package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class UserAdminController {
    private final UserServiceImpl userService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto postUser(UserCreateDto userCreateDto) {
        return userService.createUser(userCreateDto);
    }

    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    public List<UserDto> getUsers(@RequestParam List<Long> usersIdList,
                                  @RequestParam(defaultValue = "0") Integer from,
                                  @RequestParam(defaultValue = "10") Integer size) {
        if (usersIdList != null) {
            return userService.getUsers(usersIdList);
        }
        return userService.getUsers(from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteUserById(@PathVariable Long userId) {

    }


}
