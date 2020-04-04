package com.rakuten.training.web;

import java.util.ArrayList;
import java.util.List;

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
import com.rakuten.training.controller.ReviewController;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;
import com.rakuten.training.service.NoSuchProductException;
import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

@RunWith(SpringRunner.class)
@WebMvcTest({ReviewController.class})
public class ReviewControllerUnitTest {

  @Autowired MockMvc mockMvc;

  @MockBean ProductService pservice;

  @MockBean ReviewService rservice;

  @Test
  public void getReviewsForAProduct_Fails_For_Invalid_ProductID() throws Exception {
    // Arrange
    Product found = new Product("test", 123.0f, 100);
    int id = 1;
    found.setId(id);
    Mockito.when(pservice.findById(id)).thenReturn(found); // training the mock object
    Mockito.when(pservice.findById(id + 1)).thenReturn(null); // training the mock object
    // Act Assert
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products/{id}/reviews", id + 1))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }

  @Test
  public void getReviewsForProduct_Passes_For_Valid_ProductID() throws Exception {
    // Arrange
    Product prod = new Product("test", 123.0f, 100);
    int id = 1;
    prod.setId(id);

    Review r1 = new Review("self1", "this is first review", 3.5f);
    r1.setId(1);
    r1.setProduct(prod);
    Review r2 = new Review("self2", "this is second review", 4.5f);
    r2.setId(2);
    r2.setProduct(prod);
    List<Review> returnedList = new ArrayList<Review>();
    returnedList.add(r1);
    returnedList.add(r2);

    Mockito.when(pservice.findById(id)).thenReturn(prod);
    Mockito.when(rservice.findAllByProductId(id)).thenReturn(returnedList);

    List<Integer> expected = new ArrayList<>();
    expected.add(1);
    expected.add(2);

    // Act & Assert
    mockMvc
        .perform(MockMvcRequestBuilders.get("/products/{id}/reviews", id))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$[0:3:1].id", CoreMatchers.is(expected)));
  }

  @Test
  public void addReview_Passes_For_Existing_Product() throws Exception {
    // Arrange
    Review toBeAdded = new Review("Paulo Coehlo", "Unit test is boring. Alas!", 2f);
    Product prod = new Product("Sample", 123.0f, 200);
    int pid = 1; // productID
    int rid = 2; // reviewID
    prod.setId(pid);
    toBeAdded.setId(rid);
    toBeAdded.setProduct(prod);

    ObjectMapper mapper = new ObjectMapper();
    Mockito.when(rservice.addNewReview(Mockito.any(Review.class), Mockito.eq(pid))).thenReturn(rid);

    // Act Assert
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products/{id}/reviews", pid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(toBeAdded)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(
            MockMvcResultMatchers.header()
                .string("location", "/products/" + pid + "/reviews/" + rid));

    Mockito.verify(rservice, Mockito.times(1))
        .addNewReview(Mockito.any(Review.class), Mockito.eq(pid));
  }

  @Test
  public void addReview_Fails_For_Nonexisting_Product() throws Exception {
    // Arrange
    Review toBeAdded = new Review("Paulo Coehlo", "Unit test is boring. Alas!", 2f);
    Product prod = new Product("Sample", 123.0f, 200);
    int pid = 1; // productID
    int rid = 2; // reviewID
    prod.setId(pid);
    toBeAdded.setId(rid);
    toBeAdded.setProduct(prod);

    ObjectMapper mapper = new ObjectMapper();
    Mockito.when(rservice.addNewReview(Mockito.any(Review.class), Mockito.eq(pid))).thenReturn(rid);
    Mockito.when(rservice.addNewReview(Mockito.any(Review.class), Mockito.eq(pid + 1)))
        .thenThrow(new NoSuchProductException());

    // Act Assert
    mockMvc
        .perform(
            MockMvcRequestBuilders.post("/products/{id}/reviews", pid + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(toBeAdded)))
        .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}
