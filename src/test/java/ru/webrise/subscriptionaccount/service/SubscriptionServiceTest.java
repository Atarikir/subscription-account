package ru.webrise.subscriptionaccount.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.webrise.subscriptionaccount.dto.request.SubscriptionRequest;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionResponse;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionTopResponse;
import ru.webrise.subscriptionaccount.exception.EntityAlreadyExistException;
import ru.webrise.subscriptionaccount.exception.EntityNotFoundException;
import ru.webrise.subscriptionaccount.mapper.SubscriptionMapper;
import ru.webrise.subscriptionaccount.model.Subscription;
import ru.webrise.subscriptionaccount.model.User;
import ru.webrise.subscriptionaccount.repository.SubscriptionRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriptionServiceTest {

  @Mock private SubscriptionRepository subscriptionRepository;
  @Mock private SubscriptionMapper subscriptionMapper;
  @Mock private UserService userService;
  @InjectMocks private SubscriptionService subscriptionService;

  private UUID userId;
  private UUID subscriptionId;
  private String serviceName;
  private User user;
  private Subscription subscription;
  private SubscriptionRequest request;
  private SubscriptionResponse response;
  private SubscriptionTopResponse topResponse1;
  private SubscriptionTopResponse topResponse2;
  private SubscriptionTopResponse topResponse3;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    subscriptionId = UUID.randomUUID();
    serviceName = "Netflix";
    user = new User();
    subscription = new Subscription();
    request = new SubscriptionRequest(serviceName);
    response = new SubscriptionResponse(subscriptionId, serviceName, userId);
    topResponse1 = new SubscriptionTopResponse(serviceName, 100L);
    topResponse2 = new SubscriptionTopResponse("VK Музыка", 50L);
    topResponse3 = new SubscriptionTopResponse("YouTube Premium", 20L);
  }

  @AfterEach
  void tearDown() {
    userId = null;
    subscriptionId = null;
    serviceName = null;
    user = null;
    subscription = null;
    request = null;
    response = null;
    topResponse1 = null;
    topResponse2 = null;
    topResponse3 = null;
  }

  @Test
  void createSubscriptionTest() {
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionRepository.findByServiceNameAndUser_Id(serviceName, userId)).thenReturn(Optional.empty());
    when(subscriptionMapper.toSubscription(request)).thenReturn(subscription);
    when(subscriptionRepository.save(subscription)).thenReturn(subscription);
    when(subscriptionMapper.toSubscriptionResponse(subscription)).thenReturn(response);

    SubscriptionResponse result = subscriptionService.createSubscription(userId, request);

    assertNotNull(result);
    assertEquals(response, result);
    verify(userService).findUserById(userId);
    verify(subscriptionRepository).findByServiceNameAndUser_Id(serviceName, userId);
    verify(subscriptionMapper).toSubscription(request);
    verify(subscriptionRepository).save(subscription);
  }

  @Test
  void createSubscription_withEntityAlreadyExistExceptionTest() {
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionRepository.findByServiceNameAndUser_Id(serviceName, userId))
        .thenReturn(Optional.of(subscription));

    assertThrows(EntityAlreadyExistException.class, () -> subscriptionService.createSubscription(userId, request));

    verify(userService).findUserById(userId);
    verify(subscriptionRepository).findByServiceNameAndUser_Id(serviceName, userId);
  }

  @Test
  void getUserSubscriptionsTest() {
    user.setSubscriptions(List.of(subscription));
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionMapper.toSubscriptionResponseList(user.getSubscriptions())).thenReturn(List.of(response));

    List<SubscriptionResponse> result = subscriptionService.getUserSubscriptions(userId);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(1, result.size());
    assertEquals(response, result.get(0));
  }

  @Test
  void getUserSubscriptions_shouldReturnEmptyListWhenNoSubscriptionsTest() {
    user.setSubscriptions(Collections.emptyList());
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionMapper.toSubscriptionResponseList(any())).thenReturn(Collections.emptyList());

    List<SubscriptionResponse> result = subscriptionService.getUserSubscriptions(userId);

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  void deleteSubscription_shouldDeleteWhenSubscriptionExistsTest() {
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.of(subscription));

    subscriptionService.deleteSubscription(userId, subscriptionId);

    verify(userService).findUserById(userId);
    verify(subscriptionRepository).findById(subscriptionId);
    verify(subscriptionRepository).deleteByIdAndUser_Id(subscriptionId, userId);
  }

  @Test
  void deleteSubscription_shouldThrowWhenSubscriptionNotFound() {
    when(userService.findUserById(userId)).thenReturn(user);
    when(subscriptionRepository.findById(subscriptionId)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> subscriptionService.deleteSubscription(userId, subscriptionId));
  }

  @Test
  void getTopSubscriptions_shouldReturnTopSubscriptions() {
    when(subscriptionRepository.findTopThreeSubscriptions())
            .thenReturn(List.of(topResponse1, topResponse2, topResponse3));

    List<SubscriptionTopResponse> result = subscriptionService.getTopSubscriptions();

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertEquals(3, result.size());
    assertEquals(100L, result.get(0).amount());
    assertEquals(50L, result.get(1).amount());
    assertEquals(20L, result.get(2).amount());
  }

  @Test
  void getTopSubscriptions_shouldReturnEmptyListWhenNoData() {
    when(subscriptionRepository.findTopThreeSubscriptions()).thenReturn(Collections.emptyList());

    List<SubscriptionTopResponse> result = subscriptionService.getTopSubscriptions();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}
