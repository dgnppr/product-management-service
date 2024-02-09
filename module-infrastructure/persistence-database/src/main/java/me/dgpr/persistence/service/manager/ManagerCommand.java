package me.dgpr.persistence.service.manager;

import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ManagerCommand {

    private final ManagerRepository managerRepository;

    public ManagerCommand(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public ManagerEntity createNewManager(final CreateManager command) {
        ManagerEntity newManager = ManagerEntity.create(
                command.phoneNumber(),
                command.password()
        );

        return managerRepository.save(newManager);
    }

    public record CreateManager(
            String phoneNumber,
            String password) {

        public static CreateManager of(
                String phoneNumber,
                String password
        ) {
            return new CreateManager(
                    phoneNumber,
                    password
            );
        }

    }
}
