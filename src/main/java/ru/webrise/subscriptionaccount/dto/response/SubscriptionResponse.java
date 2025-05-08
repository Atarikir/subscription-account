package ru.webrise.subscriptionaccount.dto.response;

import java.util.UUID;

public record SubscriptionResponse(UUID id, String serviceName, UUID userId) {}
