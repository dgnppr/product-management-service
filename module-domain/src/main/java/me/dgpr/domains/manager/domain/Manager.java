package me.dgpr.domains.manager.domain;

import java.time.LocalDateTime;

public record Manager(
        Long id,
        LocalDateTime createdAt,
        LocalDateTime lastModifiedAt,
        LocalDateTime deletedAt,
        String phoneNumber,
        String password
) {

}
