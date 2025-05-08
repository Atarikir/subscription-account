package ru.webrise.subscriptionaccount.service;

import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webrise.subscriptionaccount.dto.request.UserRequest;
import ru.webrise.subscriptionaccount.dto.response.UserResponse;
import ru.webrise.subscriptionaccount.exception.EntityAlreadyExistException;
import ru.webrise.subscriptionaccount.exception.EntityNotFoundException;
import ru.webrise.subscriptionaccount.mapper.UserMapper;
import ru.webrise.subscriptionaccount.model.User;
import ru.webrise.subscriptionaccount.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserMapper userMapper;
  private final UserRepository userRepository;

  @Transactional
  public UserResponse createUser(UserRequest request) {
    Optional<User> optional = userRepository.findByEmail(request.email());
    if (optional.isPresent()) {
      throw new EntityAlreadyExistException(String.format("User %s already exists", request.email()));
    }
    User newUser = userMapper.toUser(request);
    userRepository.save(newUser);
    return userMapper.toUserResponse(newUser);
  }

  @Transactional(readOnly = true)
  public UserResponse getUserById(UUID id) {
    return userMapper.toUserResponse(this.findUserById(id));
  }

  @Transactional
  public UserResponse updateUser(UUID id, UserRequest request) {
    this.findUserById(id);
    User user = userMapper.toUser(request);
    user.setId(id);
    return userMapper.toUserResponse(userRepository.save(user));
  }

  @Transactional
  public void deleteUser(UUID id) {
    this.findUserById(id);
    userRepository.deleteById(id);
  }

  private User findUserById(UUID id) {
    return userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
  }
}
