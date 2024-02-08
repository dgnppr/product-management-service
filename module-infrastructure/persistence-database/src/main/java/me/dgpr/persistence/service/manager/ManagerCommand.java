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

    public ManagerEntity getOrCreateNewManager(final CreateManager command) {
        return managerRepository.findByPhoneNumber(command.phoneNumber())
                .orElseGet(() -> createNewManager(command));
    }

    private ManagerEntity createNewManager(final CreateManager command) {
        ManagerEntity newManager = ManagerEntity.newManager(
                command.phoneNumber(),
                command.password()
        );

        return managerRepository.save(newManager);
    }

    public record CreateManager(
            String phoneNumber,
            String password) {

    }
}
