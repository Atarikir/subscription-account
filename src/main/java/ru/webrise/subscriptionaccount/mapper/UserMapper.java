package ru.webrise.subscriptionaccount.mapper;

import org.mapstruct.Mapper;
import ru.webrise.subscriptionaccount.dto.request.UserRequest;
import ru.webrise.subscriptionaccount.dto.response.UserResponse;
import ru.webrise.subscriptionaccount.model.User;


@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserRequest userRequest);
    UserResponse toUserResponse(User user);
}
