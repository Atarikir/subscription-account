package ru.webrise.subscriptionaccount.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.webrise.subscriptionaccount.dto.request.UserRequest;
import ru.webrise.subscriptionaccount.dto.response.UserResponse;
import ru.webrise.subscriptionaccount.exception.EntityAlreadyExistException;
import ru.webrise.subscriptionaccount.exception.EntityNotFoundException;
import ru.webrise.subscriptionaccount.exception.EntityReferenceException;
import ru.webrise.subscriptionaccount.mapper.UserMapper;
import ru.webrise.subscriptionaccount.mapper.UserMapperImpl;
import ru.webrise.subscriptionaccount.model.Subscription;
import ru.webrise.subscriptionaccount.model.User;
import ru.webrise.subscriptionaccount.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Spy private UserMapper userMapper = new UserMapperImpl();
  @Mock private UserRepository userRepository;
  @InjectMocks private UserService userService;
  private User user;
  private UserRequest userRequest;
  private Subscription subscription;

  @BeforeEach
  void setUp() {
    user = new User();
    user.setId(UUID.fromString("7f000101-8210-186c-8182-10b6134f0000"));
    user.setEmail("test@test.com");
    user.setFirstName("Test");
    user.setLastName("Test");

    subscription = new Subscription();
    subscription.setId(UUID.randomUUID());
    subscription.setServiceName("serviceName");
    subscription.setUser(user);

    userRequest = new UserRequest("test@test.com", "Test", "Test");
  }

  @AfterEach
  void tearDown() {
    user = null;
    userRequest = null;
    subscription = null;
  }

  @Test
  void createUserTest() {
    when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.empty());
    when(userMapper.toUser(userRequest)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);

    UserResponse created = userService.createUser(userRequest);

    assertNotNull(created.id());
    assertNotNull(created.email());
    assertNotNull(created.firstName());
    assertNotNull(created.lastName());
    assertEquals(userRequest.email(), created.email());
    assertEquals(userRequest.firstName(), created.firstName());
    assertEquals(userRequest.lastName(), created.lastName());
    verify(userRepository).findByEmail(anyString());
    verify(userMapper).toUser(any(UserRequest.class));
    verify(userRepository).save(any(User.class));
    verify(userMapper).toUserResponse(any(User.class));
  }

  @Test
  void createUser_withEntityAlreadyExistExceptionTest() {
    when(userRepository.findByEmail(userRequest.email())).thenReturn(Optional.of(user));

    assertThrows(EntityAlreadyExistException.class, () -> userService.createUser(userRequest));

    verify(userRepository).findByEmail(anyString());
  }

  @Test
  void getUserByIdTest() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    UserResponse result = userService.getUserById(user.getId());

    assertNotNull(result.id());
    assertNotNull(result.email());
    assertNotNull(result.firstName());
    assertNotNull(result.lastName());
    assertEquals(userRequest.email(), result.email());
    assertEquals(userRequest.firstName(), result.firstName());
    assertEquals(userRequest.lastName(), result.lastName());
    verify(userRepository).findById(any(UUID.class));
    verify(userMapper).toUserResponse(any(User.class));
  }

  @Test
  void updateUserTest() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(userMapper.toUser(userRequest)).thenReturn(user);
    when(userRepository.save(user)).thenReturn(user);

    UserResponse updatedUser = userService.updateUser(user.getId(), userRequest);

    assertNotNull(updatedUser.id());
    assertNotNull(updatedUser.email());
    assertNotNull(updatedUser.firstName());
    assertNotNull(updatedUser.lastName());
    assertEquals(user.getId(), updatedUser.id());
    assertEquals(userRequest.email(), updatedUser.email());
    assertEquals(userRequest.firstName(), updatedUser.firstName());
    assertEquals(userRequest.lastName(), updatedUser.lastName());
    verify(userRepository).findById(any(UUID.class));
    verify(userMapper).toUser(any(UserRequest.class));
    verify(userRepository).save(any(User.class));
    verify(userMapper).toUserResponse(any(User.class));
  }

  @Test
  void deleteUserTest() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    userService.deleteUser(user.getId());

    verify(userRepository).findById(any(UUID.class));
    verify(userRepository).delete(user);
  }

  @Test
  void deleteUser_withEntityReferenceExceptionTest() {
    user.setSubscriptions(List.of(subscription));
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    assertThrows(EntityReferenceException.class, () -> userService.deleteUser(user.getId()));

    verify(userRepository).findById(any(UUID.class));
  }

  @Test
  void findUserByIdTest() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

    User result = userService.findUserById(user.getId());

    assertNotNull(result);
    verify(userRepository).findById(any(UUID.class));
  }

  @Test
  void findUserById_withEntityNotFoundExceptionTest() {
    when(userRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> userService.getUserById(user.getId()));

    verify(userRepository).findById(any(UUID.class));
  }
}
