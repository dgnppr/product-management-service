package me.dgpr.persistence.repository.manager;

import java.util.Optional;
import me.dgpr.persistence.entity.manager.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {

    Optional<ManagerEntity> findByPhoneNumber(String phoneNumber);
}
