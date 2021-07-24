package io.wotkins.newsapi.api.exceprions;

public class InvalidResourceException extends Exception{
    public InvalidResourceException(String message) {
        super(message);
    }
}
