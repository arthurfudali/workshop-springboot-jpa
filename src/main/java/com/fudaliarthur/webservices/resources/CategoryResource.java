package com.fudaliarthur.webservices.resources;

import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.services.CategoryService;
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
@RequestMapping("/categories")
@Tag(name = "Categories management", description = "Rotas para gerenciamento de categorias")
public class CategoryResource {


    private final CategoryService categoryService;

    @Autowired
    public CategoryResource(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(summary = "Busca todos as categorias", description = "Endpoint que retorna uma lista completa de todas as categorias.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
    })
    @GetMapping
    public ResponseEntity<List<Category>> findAll() {
        List<Category> categories = categoryService.findAll();
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "Busca categoria por ID", description = "Endpoint que retorna uma categoria específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Busca feita com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Category> findById(@PathVariable Long id) {
        Category category = categoryService.findById(id);
        return ResponseEntity.ok(category);
    }

    @Operation(summary = "Cria categoria", description = "Endpoint que cria uma categoria com base nas informações do corpo.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada"),
            @ApiResponse(responseCode = "400", description = "Informações faltando")
    })
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody Category cat) {
        cat = categoryService.createCategory(cat);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(cat.getId()).toUri();
        return ResponseEntity.created(uri).body(cat);
    }

    @Operation(summary = "Deleta categoria por ID", description = "Endpoint que deleta uma categoria específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Atualiza categoria por ID", description = "Endpoint que atualiza as informações de uma categoria específica.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria alterado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada"),
            @ApiResponse(responseCode = "400", description = "Informações faltando")
    })
    @PutMapping(value = "/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category cat) {
        cat = categoryService.updateCategory(id, cat);
        return ResponseEntity.ok(cat);
    }
}
