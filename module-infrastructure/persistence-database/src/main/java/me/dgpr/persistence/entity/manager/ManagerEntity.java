package me.dgpr.persistence.entity.manager;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import me.dgpr.persistence.config.BaseEntity;

@Entity
@Table(name = "manager")
public class ManagerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_id")
    private Long id;

    private String phoneNumber;

    private String password;

    protected ManagerEntity() {
    }

    private ManagerEntity(
            final String phoneNumber,
            final String password
    ) {
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public static ManagerEntity create(
            final String phoneNumber,
            final String password) {
        return new ManagerEntity(phoneNumber, password);
    }

    public Long getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }
}
