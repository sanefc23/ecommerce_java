package com.sanefc.sanefc.controllers;

import com.sanefc.sanefc.dto.SaleRequest;
import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.models.Product;
import com.sanefc.sanefc.models.Sale;
import com.sanefc.sanefc.repository.SaleRepository;
import com.sanefc.sanefc.repository.ClientRepository;
import com.sanefc.sanefc.repository.ProductRepository;

import com.sanefc.sanefc.services.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/sales")
public class SaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/all")
    public List<Sale> getAllSales() {
        return saleService.getSales();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale foundSale = saleService.getSaleById(id);
        return ResponseEntity.ok(foundSale);
    }

    @PostMapping("/add")
    public ResponseEntity<Sale> addSale(@RequestBody SaleRequest saleRequest) {
        Sale createdSale = saleService.createSale(saleRequest);
        if (createdSale != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createdSale);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Sale> updateSale(@PathVariable Long id, @RequestBody SaleRequest saleRequest) {
        Sale updatedSale = saleService.updateSale(id, saleRequest);
        if (updatedSale != null) {
            return ResponseEntity.ok(updatedSale);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedSale);
        }
    }

    @GetMapping("/invoice/{id}")
    public String generateInvoice(@PathVariable Long id) {
    return saleService.generateInvoice(id);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteSale(@PathVariable Long id) {
        Sale deletedSale = saleService.deleteSale(id);
        if(deletedSale != null){
            return "Venta eliminada.";
        } else{
            return "Error. Venta no encontrado.";
        }
    }
}