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

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;

    private ProductRepository productRepository;

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
        List<Long> productIds = saleRequest.getProductIds();
        List<Product> foundProducts;
        if (saleRequest.getProductIds() != null && !saleRequest.getProductIds().isEmpty()) {
            // List<Long> productIdsIterable = saleRequest.getProductIds();
            foundProducts = productService.getProductsByIds(productIds);
        } else {
            return null;
        }

        List<Product> saleProducts  = new ArrayList<>();;

        for (Long productId : productIds) {
            for (Product product : foundProducts) {
                if (product.getId() == productId) {
                    saleProducts.add(product);
                }
            }
        }

        double saleTotal = saleProducts.stream()
                .mapToDouble(Product::getPrice)
                .sum();

        LocalDateTime saleDate = getCurrentDateTime();
        Sale sale = new Sale();
        sale.setClient(foundClient.get());
        sale.setProducts(saleProducts);
        sale.setDate(saleDate);
        sale.setQuantity(saleRequest.getProductIds().size());
        sale.setTotal(saleTotal);
        return saleRepository.save(sale);
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

    private LocalDateTime getCurrentDateTime() {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://worldclockapi.com/api/json/utc/now"))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();
            LocalDateTime currentDateTime = LocalDateTime.parse(json.substring(7, json.length() - 2), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            return currentDateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return LocalDateTime.now();
        }
    }
}
