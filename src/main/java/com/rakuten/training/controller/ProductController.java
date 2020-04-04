package com.rakuten.training.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.rakuten.training.domain.Product;
import com.rakuten.training.service.ProductService;

@RestController
public class ProductController {
  // Here Product is considered as a Resource and this is the controller class

  @Autowired ProductService service;

  //	@RequestMapping(method = RequestMethod.GET, value = "/products")
  @GetMapping("/products")
  public List<Product> getAllProducts() {
    return service.findAll(); // by default spring converts and returns a JSONified list of product objects
  }

  @GetMapping("/products/{prod_id}") // URI path template
  public ResponseEntity<Product> getProductById(@PathVariable("prod_id") int id) {
    Product p = service.findById(id);
    if (p != null) {
      return new ResponseEntity<Product>(p, HttpStatus.OK); // 200
    } else {
      return new ResponseEntity<Product>(HttpStatus.NOT_FOUND); // 404
    }
  }

  @PostMapping("/products")
  public ResponseEntity<Product> addProduct(@RequestBody Product toBeAdded) {
    try {
      int id = service.addNewProduct(toBeAdded);
      HttpHeaders header = new HttpHeaders();
      header.setLocation(URI.create("/products/" + id));
      return new ResponseEntity<Product>(header, HttpStatus.CREATED);
    } catch (IllegalArgumentException e) {
      return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
    }
  }

  @DeleteMapping("/products/{prod_id}")
  public ResponseEntity<Product> deleteProduct(@PathVariable("prod_id") int id) {
    try {
      service.removeProduct(id);
      return new ResponseEntity<Product>(HttpStatus.NO_CONTENT); // 204
    } catch (IllegalStateException e) {
      return new ResponseEntity<Product>(HttpStatus.CONFLICT); // 409
    } catch (NullPointerException e) {
      return new ResponseEntity<Product>(HttpStatus.NOT_FOUND); // 404
    }
  }
}
