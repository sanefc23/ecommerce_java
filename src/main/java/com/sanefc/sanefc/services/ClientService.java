package com.sanefc.sanefc.services;


import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository clientRepository;

    public List<Client> getClients() {
        return clientRepository.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Client createClient(Client client) {
        return clientRepository.save(client);
    }

    public Client updateClient(Long id, Client client) {
        Optional<Client> foundClient = clientRepository.findById(id);
        if (foundClient.isEmpty()) {
            return null;
        } else {
            foundClient.get().setName(client.getName());
            foundClient.get().setEmail(client.getEmail());
            foundClient.get().setPhone(client.getPhone());
            foundClient.get().setAddress(client.getAddress());
            return clientRepository.save(foundClient.get());
        }
    }

    public Client deleteClient(Long id) {
        Optional<Client> foundClient = clientRepository.findById(id);
        if (foundClient.isPresent()) {
            clientRepository.deleteById(id);
            return foundClient.get();
        } else{
            return null;
        }
    }
}
