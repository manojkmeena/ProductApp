package com.rakuten.training.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;
import com.rakuten.training.service.NoSuchProductException;
import com.rakuten.training.service.ProductService;
import com.rakuten.training.service.ReviewService;

@RestController
public class ReviewController {

  @Autowired ReviewService service;

  @Autowired ProductService ps;

  @GetMapping("/products/{productId}/reviews")
  public ResponseEntity<List<Review>> getReviewsForAProduct(
      @PathVariable("productId") int productId) {
    Product p = ps.findById(productId);
    if (p == null) {
      return new ResponseEntity<List<Review>>(HttpStatus.NOT_FOUND);
    }
//    List<Review> reviews = service.findAllByProductId(productId);
    return new ResponseEntity<List<Review>>(service.findAllByProductId(productId), HttpStatus.OK);
  }

  @PostMapping("/products/{productId}/reviews")
  public ResponseEntity<Review> addReview(
      @PathVariable("productId") int pId, @RequestBody Review toBeAdded) {
    try {
      int rev_id = service.addNewReview(toBeAdded, pId);
      // System.out.println(rev_id);
      HttpHeaders header = new HttpHeaders();
      header.setLocation(URI.create("/products/" + pId + "/reviews/" + rev_id));
      return new ResponseEntity<Review>(toBeAdded, header, HttpStatus.CREATED);
    } catch (NoSuchProductException e) {
      return new ResponseEntity<Review>(HttpStatus.NOT_FOUND);
    }
  }
}
