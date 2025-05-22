package ru.practicum.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.dto.user.UserCreateDto;
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

    public UserDto createUser(UserCreateDto userCreateDto) {
        if (userRepository.existsByEmail(userCreateDto.getEmail())) {
            throw new ConflictPropertyConstraintException(String.format("Email %s уже зарегистрирован ",
                    userCreateDto.getEmail()));
        }
        final User user = UserMapperCustom.toUser(userCreateDto);
        return UserMapperCustom.toUserDto(userRepository.save(user));
    }

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        final Pageable pageable = PageRequest.of(0, size + from, Sort.by("id").ascending());
        Page<User> users;
        if (ids == null || ids.isEmpty()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findAllByIdIn(ids, PageRequest.of(from > 0 ? from / size : 0, size)); /// нужна ли сортировка?
        }
        return users.getContent().stream()
                .map(UserMapperCustom::toUserDto)
                .toList();
    }

    public void deleteUserById(Long id) {
        checkUserId(id);
        userRepository.deleteById(id);
    }

    private void checkUserId(long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с id " + id + " не найден."));
    }

    @Override
    public UserShortDto getUserShortById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        return UserMapperCustom.toUserShortDto(user);
    }
}
