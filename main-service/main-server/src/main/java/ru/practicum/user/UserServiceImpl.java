package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.UserDto;
import ru.practicum.dto.user.UserShortDto;
import ru.practicum.exception.ConflictPropertyConstraintException;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDto createUser(UserDto userDto) {
        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new ConflictPropertyConstraintException(String.format("Email %s уже зарегистрирован ",
                    userDto.getEmail()));
        }
        final User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userRepository.save(user));
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        final Pageable pageable = PageRequest.of(0, size + from, Sort.by("id").ascending());
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from > 0 ? from / size : 0, size)); /// нужна ли сортировка?
        }
        return users.stream()
                .map(userMapper::toUserDto)
                .toList();
    }

    public void deleteUserById(Long id) {
        checkUserId(id);
        userRepository.deleteById(id);
    }

    private void checkUserId(long id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Пользователь с id " + id + " не найден.");
        }
    }

    @Override
    public UserShortDto getUserShortById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserShortDto.builder()
                .id(user.getId())
                .name(user.getName())
                .build();
    }
}
