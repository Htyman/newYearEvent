package com.slay.groupAwards.exception;

public class AlreadyVotedException extends RuntimeException {

    public AlreadyVotedException(String message) {
        super(message);
    }
}
