package me.dgpr.persistence.fixture;

import me.dgpr.persistence.entity.manager.ManagerEntity;

public enum ManagerFixture {

    DEFAULT_MANAGER(
            "01012345678",
            "password"
    );

    private final String phoneNumber;
    private final String password;

    ManagerFixture(
            final String phoneNumber,
            final String password
    ) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public ManagerEntity create() {
        return ManagerEntity.create(
                phoneNumber,
                password
        );
    }
}
