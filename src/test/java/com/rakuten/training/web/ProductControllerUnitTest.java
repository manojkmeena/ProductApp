package com.rakuten.training.web;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rakuten.training.controller.ProductController;
import com.rakuten.training.domain.Product;
import com.rakuten.training.service.ProductService;

@RunWith(SpringRunner.class)
@WebMvcTest({ProductController.class})
public class ProductControllerUnitTest {

  @Autowired MockMvc mockMvc;

  @MockBean ProductService service;
  // this is similar to a Mockito mock which creates mock object of ProductService and
  // injects it to the ProductController.

  @Test
  public void getProductById_Returns_OK_With_Correct_Product_If_Found() throws Exception {
    // Arrange
    Product found = new Product("test", 123.0f, 100);
    int id = 1;
    found.setId(id);
    Mockito.when(service.findById(id)).thenReturn(found); // training the mock object

    // Act & Assert
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products/{id}", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is(id)));
  }

  @Test
  public void getProductById_Returns_NOTFOUND_With_Incorrect_Product() throws Exception {
    // Arrange
    Product found = new Product("test", 123.0f, 100);
    int id = 1;
    found.setId(id);
    Mockito.when(service.findById(id)).thenReturn(found); // training the mock object

    // Act & Assert
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products/{id}", id + 1))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void addProduct_Returns_CREATED_With_Valid_Product() throws Exception {
    // Arrange
    Product added = new Product("test", 123.0f, 100);
    int id = 1;
    added.setId(id);

    ObjectMapper mapper = new ObjectMapper();
    Mockito.when(service.addNewProduct(Mockito.any(Product.class))).thenReturn(id);
    
    // Act & Assert

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(added)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.header().string("location", "/products/" + id));

    Mockito.verify(service, Mockito.times(1)).addNewProduct(Mockito.any(Product.class));
  }

  @Test
  public void addProduct_Returns_BADREQUEST_With_Invalid_Product() throws Exception {
    // Arrange
    Product added = new Product("test", 12.0f, 10);
    int id = 1;
    added.setId(id);

    ObjectMapper mapper = new ObjectMapper();

    // Act & Assert
    Mockito.when(service.addNewProduct(Mockito.any(Product.class)))
        .thenThrow(new IllegalArgumentException());

    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(added)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }

  @Test
  public void deleteProduct_Returns_NOCONTENT_With_Valid_Deletion() throws Exception {
    // Arrange
    Product added = new Product("test", 123.0f, 100);
    int id = 1;
    added.setId(id);

    Mockito.doNothing().when(service).removeProduct(id);
    
    // Act & Assert
    mockMvc
    .perform(MockMvcRequestBuilders.delete("/products/{id}", id))
    .andExpect(MockMvcResultMatchers.status().isNoContent());
    
    Mockito.verify(service, Mockito.times(1)).removeProduct(id);
  }
  
  @Test
  public void deleteProduct_Returns_CONFLICT_With_Invalid_Deletion_Due_To_Excess_Value() throws Exception {
    // Arrange
    Product added = new Product("test", 20000.0f, 1000);
    int id = 1;
    added.setId(id);

    Mockito.doThrow(IllegalStateException.class).when(service).removeProduct(id);
    
    // Act & Assert
    mockMvc
    .perform(MockMvcRequestBuilders.delete("/products/{id}", id))
    .andExpect(MockMvcResultMatchers.status().isConflict());
    
    Mockito.verify(service, Mockito.times(1)).removeProduct(id);
  }
  
  @Test
  public void deleteProduct_Returns_NOTFOUND_With_Unavailable_Product() throws Exception {
    // Arrange
    Product added = new Product("test", 20000.0f, 1000);
    int id = 1;
    added.setId(id);

    Mockito.doNothing().when(service).removeProduct(id);
    Mockito.doThrow(NullPointerException.class).when(service).removeProduct(id+1);
    
    // Act & Assert
    mockMvc
    .perform(MockMvcRequestBuilders.delete("/products/{id}", id+1))
    .andExpect(MockMvcResultMatchers.status().isNotFound());
    
    Mockito.verify(service, Mockito.times(1)).removeProduct(id+1);
  }
}
