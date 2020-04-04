package com.rakuten.training.service;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.mockito.Mockito;

import com.rakuten.training.dao.ProductDAO;
import com.rakuten.training.domain.Product;

public class ProductServiceImplTest {

  @Test
  public void addNewProduct_Returns_ValidId_When_ProductVal_GTE_MinVal() {
    // Arrange
	  ProductServiceImpl serv = new ProductServiceImpl();
	  Product toBeAdded = new Product("test", 10000, 1); // N.B: 10000*1 >= 10000
	  ProductDAO mockDAO = Mockito.mock(ProductDAO.class); // mock object for product dao
	  Product saved = new Product("test", 10000, 1);
	  saved.setId(1);
	  Mockito.when(mockDAO.save(toBeAdded)).thenReturn(saved);
	  serv.setDao(mockDAO);
    // Act
	  int id = serv.addNewProduct(toBeAdded);
    // Assert
	  assertTrue(id>0);
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void addNewProduct_Throws_Error_When_ProductVal_LT_MinVal() {
	  // Arrange
	  ProductServiceImpl serv = new ProductServiceImpl();
	  Product toBeAdded = new Product("test", 9999, 1); // N.B: 10000*1 >= 10000
	  // Act
	  serv.addNewProduct(toBeAdded);
  }
  
  @Test
  public void removeProduct_Actually_Deletes() {
	  //Arrange
	  ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
	  ProductServiceImpl service = new ProductServiceImpl();
	  Product deleted = new Product("test", 200, 1);
	  int deletionId = 1;
	  deleted.setId(deletionId);
	  service.setDao(mockDAO);
	  Mockito.when(mockDAO.findById(deletionId)).thenReturn(deleted);
	  //Act & Assert
	  service.removeProduct(deletionId);
	  Mockito.verify(mockDAO, Mockito.times(1)).deleteById(deletionId);
  }
  
  @Test(expected = IllegalStateException.class)
  public void removeProduct_Throws_Error_When_ProductVal_GTE_100000() {
	  // Arrange
	  ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
	  ProductServiceImpl service = new ProductServiceImpl();
	  Product deleted = new Product("test", 10000, 100);
	  int deletionId = 1;
	  deleted.setId(deletionId);
	  service.setDao(mockDAO);
	  Mockito.when(mockDAO.findById(deletionId)).thenReturn(deleted);
	  //Act
	  service.removeProduct(deletionId);
  }
  
  @Test
  public void findById_Actually_Finds() {
	  // Arrange
	  ProductDAO mockDAO = Mockito.mock(ProductDAO.class);
	  ProductServiceImpl service = new ProductServiceImpl();
	  Product finding = new Product("test", 100, 100);
	  int id = 1;
	  finding.setId(id);
	  service.setDao(mockDAO);
	  Mockito.when(mockDAO.findById(id)).thenReturn(finding);
	  // Act
	  Product p = service.findById(id);
	  // Assert
	  assertTrue(p == finding);
  }
}
