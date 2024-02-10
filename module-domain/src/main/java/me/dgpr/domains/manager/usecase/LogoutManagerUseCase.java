package me.dgpr.domains.manager.usecase;

import java.time.Duration;

public interface LogoutManagerUseCase {

    void command(Command command);

    record Command(
            long managerId,
            String token,
            Duration duration
    ) {

    }

}
