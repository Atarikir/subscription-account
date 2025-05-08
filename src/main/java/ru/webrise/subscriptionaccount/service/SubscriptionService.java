package ru.webrise.subscriptionaccount.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.webrise.subscriptionaccount.dto.request.SubscriptionRequest;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionResponse;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionTopResponse;
import ru.webrise.subscriptionaccount.exception.EntityAlreadyExistException;
import ru.webrise.subscriptionaccount.exception.EntityNotFoundException;
import ru.webrise.subscriptionaccount.mapper.SubscriptionMapper;
import ru.webrise.subscriptionaccount.model.Subscription;
import ru.webrise.subscriptionaccount.model.User;
import ru.webrise.subscriptionaccount.repository.SubscriptionRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final SubscriptionMapper subscriptionMapper;
  private final UserService userService;

  @Transactional
  public SubscriptionResponse createSubscription(UUID userId, SubscriptionRequest request) {
    Optional<Subscription> optional =
        subscriptionRepository.findByServiceNameAndUser_Id(request.serviceName(), userId);
    if (optional.isPresent()) {
      throw new EntityAlreadyExistException(
          String.format("Subscription %s already exists for the user", request.serviceName()));
    }
    Subscription newSubscription = subscriptionMapper.toSubscription(request);
    User user = userService.findUserById(userId);
    newSubscription.setUser(user);
    log.info("Creating subscription {} for user: {} ", newSubscription.getServiceName(), user.getEmail());
    subscriptionRepository.save(newSubscription);
    return subscriptionMapper.toSubscriptionResponse(newSubscription);
  }

  @Transactional(readOnly = true)
  public List<SubscriptionResponse> getUserSubscriptions(UUID userId) {
    List<Subscription> subscriptions = userService.findUserById(userId).getSubscriptions();
    return subscriptionMapper.toSubscriptionResponseList(subscriptions);
  }

  @Transactional
  public void deleteSubscription(UUID userId, UUID subscriptionId) {
    userService.findUserById(userId);
    subscriptionRepository.findById(subscriptionId)
        .orElseThrow(() -> new EntityNotFoundException("Subscription not found"));
    subscriptionRepository.deleteByIdAndUser_Id(subscriptionId, userId);
  }

  @Transactional(readOnly = true)
  public List<SubscriptionTopResponse> getTopSubscriptions() {
    return subscriptionRepository.findTopThreeSubscriptions();
  }
}
