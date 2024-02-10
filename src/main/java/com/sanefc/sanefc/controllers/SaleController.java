package com.sanefc.sanefc.controllers;

import com.sanefc.sanefc.dto.SaleRequest;
import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.models.Product;
import com.sanefc.sanefc.models.Sale;
import com.sanefc.sanefc.repository.SaleRepository;
import com.sanefc.sanefc.repository.ClientRepository;
import com.sanefc.sanefc.repository.ProductRepository;

import com.sanefc.sanefc.services.SaleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Get Sales", description = "Permite obtener todos las ventas.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"),})
    @GetMapping("/all")
    public List<Sale> getAllSales() {
        return saleService.getSales();
    }

    @Operation(summary = "Get Sale by ID", description = "Permite obtener una venta por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @GetMapping("/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Optional<Sale> foundSale = saleService.getSaleById(id);
        return foundSale.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create Sale", description = "Permite crear una nuevo venta.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa")})
    @PostMapping("/add")
    public ResponseEntity<Sale> addSale(@RequestBody SaleRequest saleRequest) {
        Sale createdSale = saleService.createSale(saleRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSale);
    }

    @Operation(summary = "Generate Invoice by ID", description = "Permite generar un ticket por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Venta no encontrada")})
    @GetMapping("/invoice/{id}")
    public String generateInvoice(@PathVariable Long id) {
        return saleService.generateInvoice(id);
    }

    @Operation(summary = "Delete Sale by ID", description = "Permite eliminar una venta por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Venta no encontrado")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Sale> deleteSale(@PathVariable Long id) {
        Optional<Sale> deletedSale = Optional.ofNullable(saleService.deleteSale(id));
        return deletedSale.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}