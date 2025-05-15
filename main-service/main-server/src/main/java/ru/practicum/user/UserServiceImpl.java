package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserCreateDto userCreateDto) {
        User user = UserMapper.toModel(userCreateDto);
     return UserMapper.toDto(userRepository.save(user));
    }

    public List<UserDto> getUsers(int from, int size) {
        List<User> userList = userRepository.findAll(PageRequest.of(from > 0 ? from / size : 0, size)).toList();
        if (userList.isEmpty()) {
            return List.of();
        }
        return userList.stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public List<UserDto> getUsers(List<Long> usersList) {
        return userRepository.findAllById(usersList).stream()
                .map(UserMapper::toDto)
                .toList();
    }

    public void deleteUserById(long id) {
        checkUserId(id);
        userRepository.deleteById(id);
    }

    private void checkUserId(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
    }
}
