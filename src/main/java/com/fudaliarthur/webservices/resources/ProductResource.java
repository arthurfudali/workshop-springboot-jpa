package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.dto.ProductRequestDTO;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/products")
@Tag(name = "Product management", description = "Rotas para gerenciamento de produtos")
public class ProductResource {


    private final ProductService productService;

    @Autowired
    public ProductResource(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "Busca todos os produtos", description = "Endpoint que retorna uma lista completa de todos os produtos.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<Product>> findAll() {
        List<Product> products = productService.findAll();
        return ResponseEntity.ok(products);
    }

    @Operation(summary = "Busca produto por ID", description = "Endpoint que retorna um produto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        Product product = productService.findById(id);
        return ResponseEntity.ok(product);
    }

    @Operation(summary = "Cria produto", description = "Endpoint que cria um produto com base nas informações do corpo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produto criado"),
            @ApiResponse(responseCode = "400", description = "Informações faltando")
    })
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        Product createdProduct = productService.createProduct(productRequestDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(createdProduct.getId()).toUri();
        return ResponseEntity.created(uri).body(createdProduct);
    }

    @Operation(summary = "Deleta produto por ID", description = "Endpoint que deleta um produto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produto deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza produto por ID", description = "Endpoint que atualiza as informações de um produto específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produto alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Produto não encontrado"),
            @ApiResponse(responseCode = "400", description = "Informações faltando")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDTO productDTO) {
        Product updatedProduct = productService.updateProduct(id, productDTO);
        return ResponseEntity.ok(updatedProduct);
    }
}
