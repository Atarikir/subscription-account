package ru.webrise.subscriptionaccount.exception;

public class EntityAlreadyExistException extends RuntimeException {
  public EntityAlreadyExistException(String message) {
    super(message);
  }
}
