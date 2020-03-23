package com.rakuten.training.dao;

import java.util.List;

import com.rakuten.training.domain.Product;

public interface ProductDAO {
  // Product Data Access Object

  Product save(Product toBeSaved);

  Product findById(int id);

  List<Product> findAll();

  void deleteById(int id);
}
