package com.sanefc.sanefc.repository;

import com.sanefc.sanefc.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

