package com.springaopmongo.auditing.repositories;

import com.springaopmongo.auditing.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> { }
