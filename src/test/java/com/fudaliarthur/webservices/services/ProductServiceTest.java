package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.dto.ProductRequestDTO;
import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.repositories.CategoryRepository;
import com.fudaliarthur.webservices.repositories.ProductRepository;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @InjectMocks
    private ProductService productService;

    private Product p1, p2;
    private Category cat1, cat2;

    @BeforeEach
    void setUp() {
        p1 = new Product(1L, "The Lord of the Rings", "Lorem ipsum dolor sit amet, consectetur.", 90.5, "");
        p2 = new Product(2L, "Smart TV", "Nulla eu imperdiet purus. Maecenas ante.", 2190.0, "");
        cat1 = new Category(1L, "Electronics");
        cat2 = new Category(2L, "Books");
        p1.getCategories().add(cat2);
        p2.getCategories().add(cat1);
    }

    @Test
    @DisplayName("Should find all products")
    void findAll() {
        List<Product> producst = Arrays.asList(p1, p2);
        when(productRepository.findAll()).thenReturn(producst);

        List<Product> result = productService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(producst);

        verify(productRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Should find a product by ID when it exists")
    void findProductById() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(p1));

        Product result = productService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(p1);
        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when product not found")
    void findProductByIdNotFound() {
        Long id = 99L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.findById(id);
        });

        verify(productRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should create product successfully")
    void createProduct() {
        Product newProduct = new Product(null, "Teste", "Descricao", 100.0, null);
        ProductRequestDTO productRequestDTO = new ProductRequestDTO("Teste", "Descricao", 100.0, null, null);
        Product savedProduct = new Product(3L, "Teste", "Descricao", 100.0, null);

        when(productRepository.save(newProduct)).thenReturn(savedProduct);

        Product result = productService.createProduct(productRequestDTO);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedProduct);
        verify(productRepository, times(1)).save(newProduct);

    }

    @Test
    @DisplayName("Should delete product when product exists")
    void deleteProduct() {
        Long id = 1L;
        when(productRepository.findById(id)).thenReturn(Optional.of(p1));
        doNothing().when(productRepository).delete(p1);

        productService.deleteProduct(id);

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, times(1)).delete(p1);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent product")
    void deleteProductNotFound() {
        Long id = 99L;
        when(productRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            productService.deleteProduct(id);
        });

        verify(productRepository, times(1)).findById(id);
        verify(productRepository, never()).delete(any());
    }

    @Test
    void updateProduct() {
        Long id = 1L;
        List<Category> categories = Arrays.asList(cat1, cat2);
        ProductRequestDTO productDetails = new ProductRequestDTO("Alterado", "NovaDescrição", 10.0, "nova-img.jpg", categories);

        when(productRepository.getReferenceById(id)).thenReturn(p1);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(cat1));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(cat2));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.updateProduct(id, productDetails);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Alterado");
        assertThat(result.getDescription()).isEqualTo("NovaDescrição");
        assertThat(result.getPrice()).isEqualTo(10.0);
        assertThat(result.getImgUrl()).isEqualTo("nova-img.jpg");

        verify(productRepository, times(1)).getReferenceById(id);
        verify(productRepository, times(1)).save(p1);
    }
}