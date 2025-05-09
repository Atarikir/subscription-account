package ru.webrise.subscriptionaccount.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.webrise.subscriptionaccount.dto.request.SubscriptionRequest;
import ru.webrise.subscriptionaccount.dto.response.SubscriptionResponse;
import ru.webrise.subscriptionaccount.model.Subscription;
import ru.webrise.subscriptionaccount.model.User;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

    Subscription toSubscription(SubscriptionRequest subscriptionRequest);

    @Mapping(target = "userId", source = "user")
    SubscriptionResponse toSubscriptionResponse(Subscription subscription);

    List<SubscriptionResponse> toSubscriptionResponseList(List<Subscription> subscriptions);

    default UUID map(User user){
        return user.getId();
    }
}
