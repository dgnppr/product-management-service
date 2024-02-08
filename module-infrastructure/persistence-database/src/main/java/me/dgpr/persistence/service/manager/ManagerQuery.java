package me.dgpr.persistence.service.manager;

import me.dgpr.persistence.entity.manager.ManagerEntity;
import me.dgpr.persistence.repository.manager.ManagerRepository;
import me.dgpr.persistence.service.manager.exception.NotFoundManagerException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class ManagerQuery {

    private final ManagerRepository managerRepository;

    public ManagerQuery(ManagerRepository managerRepository) {
        this.managerRepository = managerRepository;
    }

    public ManagerEntity findById(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new NotFoundManagerException(managerId.toString()));
    }

    public ManagerEntity findByPhoneNumber(String phoneNumber) {
        return managerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new NotFoundManagerException(phoneNumber));
    }
}
