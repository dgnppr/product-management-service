package me.dgpr.persistence.service.manager;

import me.dgpr.common.exception.NotFoundException;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ManagerQuery {

    private final ManagerRepository managerRepository;

    public ManagerQuery(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public ManagerEntity findById(final long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundException(
                        "사장님",
                        String.valueOf(managerId))
                );
    }

    public ManagerEntity findByPhoneNumber(final String phoneNumber) {
        return managerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundException(
                        "사장님",
                        String.valueOf(phoneNumber))
                );
    }

    public boolean existsByPhoneNumber(final String phoneNumber) {
        return managerRepository.existsByPhoneNumber(phoneNumber);
    }
}
