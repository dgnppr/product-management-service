package me.dgpr.fixture.manager;

import java.time.LocalDateTime;
import me.dgpr.domains.manager.domain.Manager;

public enum ManagerFixture {

    DEFAULT_MANAGER(
            1L,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null,
            "01012345678",
            "password"
    );

    private final Long id;
    private final LocalDateTime createdAt;
    private final LocalDateTime lastModifiedAt;
    private final LocalDateTime deletedAt;
    private final String phoneNumber;
    private final String password;

    ManagerFixture(Long id, LocalDateTime createdAt, LocalDateTime lastModifiedAt,
            LocalDateTime deletedAt, String phoneNumber, String password) {
        this.id = id;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.deletedAt = deletedAt;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public Manager instance() {
        return new Manager(
                id,
                createdAt,
                lastModifiedAt,
                deletedAt,
                phoneNumber,
                password
        );
    }
}
