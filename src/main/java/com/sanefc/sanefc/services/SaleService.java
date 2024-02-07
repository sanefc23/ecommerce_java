package com.sanefc.sanefc.services;

import com.sanefc.sanefc.dto.SaleRequest;
import com.sanefc.sanefc.models.Client;
import com.sanefc.sanefc.models.Product;
import com.sanefc.sanefc.models.Sale;
import com.sanefc.sanefc.repository.ClientRepository;
import com.sanefc.sanefc.repository.ProductRepository;
import com.sanefc.sanefc.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;

    public List<Sale> getSales() {
        return saleRepository.findAll();
    }

    public Optional<Sale> getSaleById(Long id) {
        return saleRepository.findById(id);
    }

    public Sale createSale(SaleRequest saleRequest) {
        Optional<Client> foundClient = clientService.getClientById(saleRequest.getClientId());
        if (foundClient.isEmpty()) {
            return null;
        }
        List<Product> foundProducts;
        if (saleRequest.getProductIds() != null && !saleRequest.getProductIds().isEmpty()) {
            List<Long> productIdsIterable = saleRequest.getProductIds();
            foundProducts = productService.getProductsByIds(productIdsIterable);
        } else {
            return null;
        }
        LocalDateTime saleDate = LocalDateTime.now();
        Sale sale = new Sale();
        sale.setClient(foundClient.get());
        sale.setProducts(foundProducts);
        sale.setDate(saleDate);
        sale.setQuantity(saleRequest.getQuantity());
        sale.setTotal(saleRequest.getTotal());
        return saleRepository.save(sale);
    }

    public Sale updateSale(Long id, SaleRequest saleRequest) {
        Optional<Client> foundClient = clientService.getClientById(saleRequest.getClientId());

        if (saleRequest.getProductIds() != null && !saleRequest.getProductIds().isEmpty()) {
            List<Product> foundProducts = productService.getProductsByIds(saleRequest.getProductIds());

            Sale existingSale = saleRepository.findById(id).orElse(null);
            if (existingSale != null) {
                existingSale.setClient(foundClient.orElse(null));
                existingSale.setProducts(foundProducts);
                existingSale.setQuantity(saleRequest.getQuantity());
                existingSale.setTotal(saleRequest.getTotal());
                return saleRepository.save(existingSale);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    public String generateInvoice(Long id) {
        Optional<Sale> foundSale = saleRepository.findById(id);
        List<Product> products = foundSale.get().getProducts();

        if (foundSale.isEmpty()) {
            return "Venta no encontrada";
        } else {
            StringBuilder template = new StringBuilder();
            template.append("Factura de compra # ")
                    .append(foundSale.get().getId())
                    .append(".\nCliente: ")
                    .append(foundSale.get().getClient().getName())
                    .append(" || Email: ")
                    .append(foundSale.get().getClient().getEmail())
                    .append("\nDomicilio: ")
                    .append(foundSale.get().getClient().getAddress())
                    .append(" || Teléfono: ")
                    .append(foundSale.get().getClient().getPhone())
                    .append("\n==============================================\n")
                    .append("Productos: \n");
            for (Product product : products) {
                template.append(product.getName())
                        .append("\n");
            }
            template.append("==============================================\n")
                    .append("Cantidad: ")
                    .append(foundSale.get().getQuantity())
                    .append("\nPrecio total: ")
                    .append(foundSale.get().getTotal());

            String invoice = template.toString();
            return invoice;
        }
    }

    public Sale deleteSale(Long id){
        Optional<Sale> foundSale = saleRepository.findById(id);
        if(foundSale.isPresent()){
            saleRepository.deleteById(id);
            return foundSale.get();
        }else{
            return null;
        }
    }
}
