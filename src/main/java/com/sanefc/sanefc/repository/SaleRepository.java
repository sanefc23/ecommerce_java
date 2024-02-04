package com.sanefc.sanefc.repository;

import com.sanefc.sanefc.models.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Long> {
}