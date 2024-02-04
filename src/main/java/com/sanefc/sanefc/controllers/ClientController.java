package com.sanefc.sanefc.controllers;

import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.repository.ClientRepository;
import com.sanefc.sanefc.services.ClientService;
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
    @GetMapping("/all")
    public List<Client> getClients() {
        return clientService.getClients();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(client);
    }

    @PostMapping("/add")
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client createdClient = clientService.createClient(client);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdClient);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Client> update(@PathVariable Long id, @RequestBody Client client) {
        Client updatedClient = clientService.updateClient(id, client);
        if(updatedClient != null){
            return ResponseEntity.ok(updatedClient);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(updatedClient);
        }
    }
    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Client deletedClient = clientService.deleteClient(id);
        if(deletedClient != null){
            return "Cliente eliminado.";
        } else{
            return "Error. Cliente no encontrado.";
        }
    }
}