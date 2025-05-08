package ru.webrise.subscriptionaccount.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SubscriptionRequest(
    @NotNull(message = "Fill in the service name")
        @Size(min = 1, message = "Service name can't be less than 1")
        String serviceName) {}
