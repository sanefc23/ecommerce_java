package com.sanefc.sanefc.repository;

import com.sanefc.sanefc.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
