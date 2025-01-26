package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.InvalidCategoryNameException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {
  private static final long MAX_CATEGORIES_SIZE = 7;

  @Test
  void categoryNotFoundExceptionShouldBeThrown(@Mock CategoryRepository categoryRepository) {
    final String username = "not_found";
    final UUID id = UUID.randomUUID();

    when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.empty());

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
            id,
            "",
            username,
            true
    );

    CategoryNotFoundException ex = Assertions.assertThrows(
            CategoryNotFoundException.class,
            () -> categoryService.update(categoryJson)
    );
    Assertions.assertEquals(
            "Can`t find category by id: '" + id + "'",
            ex.getMessage()
    );
  }

  @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
  @ParameterizedTest
  void categoryNameArchivedShouldBeDenied(String catName, @Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    final UUID id = UUID.randomUUID();
    final CategoryEntity cat = new CategoryEntity();

    when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.of(
                    cat
            ));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
            id,
            catName,
            username,
            true
    );

    InvalidCategoryNameException ex = Assertions.assertThrows(
            InvalidCategoryNameException.class,
            () -> categoryService.update(categoryJson)
    );
    Assertions.assertEquals(
            "Can`t add category with name: '" + catName + "'",
            ex.getMessage()
    );
  }

  @Test
  void onlyTwoFieldsShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
    final String username = "duck";
    final UUID id = UUID.randomUUID();
    final CategoryEntity cat = new CategoryEntity();
    cat.setId(id);
    cat.setUsername(username);
    cat.setName("Магазины");
    cat.setArchived(false);

    when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
            .thenReturn(Optional.of(
                    cat
            ));
    when(categoryRepository.save(any(CategoryEntity.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(
            id,
            "Бары",
            username,
            true
    );

    categoryService.update(categoryJson);
    ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
    verify(categoryRepository).save(argumentCaptor.capture());
    assertEquals("Бары", argumentCaptor.getValue().getName());
    assertEquals("duck", argumentCaptor.getValue().getUsername());
    assertTrue(argumentCaptor.getValue().isArchived());
    assertEquals(id, argumentCaptor.getValue().getId());
  }
  @Test
  void getAllCategories_ShouldReturnAllCategoriesIncludingArchived(@Mock CategoryRepository categoryRepository) {
    String username = "testUser";
    List<CategoryEntity> categories = List.of(
            new CategoryEntity(UUID.randomUUID(), "Category1", username, false),
            new CategoryEntity(UUID.randomUUID(), "Category2", username, true)
    );

    when(categoryRepository.findAllByUsernameOrderByName(username)).thenReturn(categories);

    CategoryService categoryService = new CategoryService(categoryRepository);

    List<CategoryJson> result = categoryService.getAllCategories(username, false);

    assertNotNull(result);
    assertEquals(2, result.size());
    verify(categoryRepository, times(1)).findAllByUsernameOrderByName(username);
  }
  @Test
  void getAllCategories_ShouldReturnOnlyNonArchivedCategories(@Mock CategoryRepository categoryRepository) {
    // Arrange
    String username = "testUser";
    List<CategoryEntity> categories = List.of(
            new CategoryEntity(UUID.randomUUID(), "Category1", username, false),
            new CategoryEntity(UUID.randomUUID(), "Category2", username, true)
    );

    when(categoryRepository.findAllByUsernameOrderByName(username)).thenReturn(categories);

    CategoryService categoryService = new CategoryService(categoryRepository);

    // Act
    List<CategoryJson> result = categoryService.getAllCategories(username, true);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.size());
    assertFalse(result.get(0).archived());
    verify(categoryRepository, times(1)).findAllByUsernameOrderByName(username);
  }

  @Test
  void update_ShouldUpdateCategorySuccessfully(@Mock CategoryRepository categoryRepository) {
    // Arrange
    String username = "testUser";
    UUID categoryId = UUID.randomUUID();
    CategoryEntity categoryEntity = new CategoryEntity(categoryId, "Category1", username, false);

    when(categoryRepository.findByUsernameAndId(username, categoryId)).thenReturn(Optional.of(categoryEntity));
    when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(categoryId, "UpdatedCategory", username, false);

    // Act
    CategoryJson result = categoryService.update(categoryJson);

    // Assert
    assertNotNull(result);
    assertEquals("UpdatedCategory", result.name());
    verify(categoryRepository, times(1)).findByUsernameAndId(username, categoryId);
    verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
  }

  @Test
  void save_ShouldThrowTooManyCategoriesException_WhenExceedingLimit(@Mock CategoryRepository categoryRepository) {
    // Arrange
    String username = "testUser";
    String categoryName = "NewCategory";

    when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(MAX_CATEGORIES_SIZE + 1);

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(null, categoryName, username, false);

    // Act & Assert
    TooManyCategoriesException exception = assertThrows(TooManyCategoriesException.class, () -> {
      categoryService.save(categoryJson);
    });

    assertEquals("Can`t add over than 8 categories for user: '" + username + "'", exception.getMessage());
    verify(categoryRepository, times(1)).countByUsernameAndArchived(username, false);
    verify(categoryRepository, never()).save(any(CategoryEntity.class));
  }

  @Test
  void save_ShouldSaveCategorySuccessfully(@Mock CategoryRepository categoryRepository) {
    // Arrange
    String username = "testUser";
    String categoryName = "NewCategory";

    when(categoryRepository.countByUsernameAndArchived(username, false)).thenReturn(MAX_CATEGORIES_SIZE - 1);
    when(categoryRepository.save(any(CategoryEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

    CategoryService categoryService = new CategoryService(categoryRepository);

    CategoryJson categoryJson = new CategoryJson(null, categoryName, username, false);

    // Act
    CategoryEntity result = categoryService.save(categoryJson);

    // Assert
    assertNotNull(result);
    assertEquals(categoryName, result.getName());
    assertEquals(username, result.getUsername());
    assertFalse(result.isArchived());
    verify(categoryRepository, times(1)).countByUsernameAndArchived(username, false);
    verify(categoryRepository, times(1)).save(any(CategoryEntity.class));
  }
}