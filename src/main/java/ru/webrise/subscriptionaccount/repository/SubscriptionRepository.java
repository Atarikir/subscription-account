package ru.webrise.subscriptionaccount.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionTopResponse;
import ru.webrise.subscriptionaccount.model.Subscription;

public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

  Optional<Subscription> findByServiceNameAndUser_Id(String serviceName, UUID userId);

//  List<Subscription> findAllByUser_Id(UUID userId);

  @Modifying
  void deleteByIdAndUser_Id(UUID id, UUID userId);

  @Query(
      value =
          "SELECT s.service_name, COUNT(s) as count FROM subscriptions s GROUP BY s.service_name ORDER BY count DESC LIMIT 3",
      nativeQuery = true)
  List<SubscriptionTopResponse> findTopThreeSubscriptions();
}
