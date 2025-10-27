package com.fudaliarthur.webservices.services;

import com.fudaliarthur.webservices.entities.Category;
import com.fudaliarthur.webservices.entities.Product;
import com.fudaliarthur.webservices.repositories.CategoryRepository;
import com.fudaliarthur.webservices.services.exceptions.DatabaseException;
import com.fudaliarthur.webservices.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    private Category cat1, cat2;
    private Product product1;

    @BeforeEach
    void setUp() {
        cat1 = new Category(1L, "Electronics");
        cat2 = new Category(2L, "Computers");
        product1 = new Product(2L, "Smart TV", "Nam eleifend", 1250.0, "");
        product1.getCategories().add(cat1);

    }

    @Test
    @DisplayName("Should find all categories")
    void findAll() {
        List<Category> categories = Arrays.asList(cat1, cat2);
        when(categoryRepository.findAll()).thenReturn(categories);
        List<Category> result = categoryService.findAll();

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(categories);

        verify(categoryRepository, times(1)).findAll();

    }

    @Test
    @DisplayName("Should find category by id when it exists")
    void findByIdCategoryFound() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(cat1));

        Category result = categoryService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(cat1);

        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should throw exception when category not found")
    void findByIdCategoryNotFound() {
        Long id = 99L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.findById(id);
        });

        verify(categoryRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Should create category successfully")
    void createCategory() {
        Category newCat = new Category(null, "Test");
        Category savedCat = new Category(3L, "Test");

        when(categoryRepository.save(newCat)).thenReturn(savedCat);

        Category result = categoryService.createCategory(newCat);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(savedCat);
        verify(categoryRepository, times(1)).save(newCat);
    }

    @Test
    @DisplayName("Should delete category when it exists")
    void deleteCategoryById() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(cat1));
        doNothing().when(categoryRepository).delete(cat1);

        categoryService.deleteCategoryById(id);

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).delete(cat1);
    }

    @Test
    @DisplayName("Should thrown exception when deleting non-existing category")
    void deleteCategoryByIdNotFound() {
        Long id = 99L;
        when(categoryRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.deleteCategoryById(id);
        });

        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, never()).delete(any());
    }

    @Test
    @DisplayName("Should throw exception when deleting a category that is associated with a product")
    void deleteCategoryThatIsBeingUsed() {
        Long id = 1L;
        when(categoryRepository.findById(id)).thenReturn(Optional.of(cat1));
        doThrow(DataIntegrityViolationException.class)
                .when(categoryRepository).delete(cat1);

        assertThrows(DatabaseException.class, () -> {
            categoryService.deleteCategoryById(id);
        });
        verify(categoryRepository, times(1)).findById(id);
        verify(categoryRepository, times(1)).delete(cat1);

    }

    @Test
    @DisplayName("Should update category successfully")
    void updateCategory() {
        Long id = 1L;
        Category categoryDetails = new Category(null, "Electronics update");
        when(categoryRepository.getReferenceById(id)).thenReturn(cat1);
        when(categoryRepository.save(any(Category.class))).thenAnswer(i -> i.getArgument(0));

        Category result = categoryService.updateCategory(id, categoryDetails);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Electronics update");

        verify(categoryRepository, times(1)).getReferenceById(id);
        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    void updateCategoryNotFound() {
        Long id = 99L;
        when(categoryRepository.getReferenceById(id)).thenThrow(ResourceNotFoundException.class);

        assertThrows(ResourceNotFoundException.class, () -> {
            categoryService.updateCategory(id, null);
        });

        verify(categoryRepository, times(1)).getReferenceById(id);
        verify(categoryRepository, never()).save(any());
    }
}