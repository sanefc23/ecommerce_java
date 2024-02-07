package com.sanefc.sanefc.controllers;

import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.repository.ClientRepository;
import com.sanefc.sanefc.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Operation(summary = "Get Clients", description = "Permite obtener todos los clientes.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"),})
    @GetMapping("/all")
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @Operation(summary = "Get Client by ID", description = "Permite obtener un cliente por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> foundClient = clientService.getClientById(id);
        return foundClient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Create Client", description = "Permite crear un nuevo cliente.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa")})
    @PostMapping("/add")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);
    }


    @Operation(summary = "Update Client by ID", description = "Permite actualizar un cliente por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    @PutMapping("/update/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        Optional<Client> updatedClient = Optional.ofNullable(clientService.updateClient(id, client));
        return updatedClient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Delete Client by ID", description = "Permite eliminar un cliente por ID.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Operación exitosa"), @ApiResponse(responseCode = "404", description = "Cliente no encontrado")})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Client> delete(@PathVariable Long id) {
        Optional<Client> deletedClient = Optional.ofNullable(clientService.deleteClient(id));
        return deletedClient.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
}