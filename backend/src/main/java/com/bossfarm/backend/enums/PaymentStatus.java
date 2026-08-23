package com.bossfarm.backend.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum PaymentStatus {
    UNPAID("unpaid"),
    PAID("paid"),
    REFUNDED("refunded");

    private final String value;

    PaymentStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static PaymentStatus fromString(String text) {
        for (PaymentStatus paymentStatus : PaymentStatus.values()) {
            if (paymentStatus.value.equalsIgnoreCase(text) || paymentStatus.name().equalsIgnoreCase(text)) {
                return paymentStatus;
            }
        }
        throw new IllegalArgumentException("Unknown payment status: " + text);
    }
}
