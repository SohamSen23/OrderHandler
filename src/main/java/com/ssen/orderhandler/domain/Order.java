package com.ssen.orderhandler.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.apache.kafka.common.utils.Time;

import java.security.Timestamp;

@Data
@Builder
public class Order {
    @NonNull
    private Long id;
    private Long orderedAt;
    private Long timeTaken;
    @NonNull
    private String customerFirstName;
}
