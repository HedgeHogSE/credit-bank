package ru.neoflex.deal.exception;

public class ScoringRejectedException extends RuntimeException {
    public ScoringRejectedException(String message) {
        super(message);
    }
}