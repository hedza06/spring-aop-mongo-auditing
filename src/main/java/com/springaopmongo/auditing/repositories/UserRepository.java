package com.springaopmongo.auditing.repositories;

import com.springaopmongo.auditing.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> { }
