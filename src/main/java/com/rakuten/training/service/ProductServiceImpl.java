package com.rakuten.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.dao.ProductDAO;
import com.rakuten.training.domain.Product;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

  ProductDAO dao; // = new ProductDAOInMemImpl();

  @Autowired
  public void setDao(ProductDAO dao) {
    this.dao = dao;
  }

  @Override
  public int addNewProduct(Product toBeAdded) {
    if (toBeAdded.getPrice() * toBeAdded.getQoh() >= 10000) {
      Product added = dao.save(toBeAdded);
      return added.getId();
    } else {
      throw new IllegalArgumentException("Monetory value of product is less than 10000");
    }
  }

  @Override
  public void removeProduct(int id) {
    Product existing = dao.findById(id);
    if (existing != null) {
      if (existing.getPrice() * existing.getQoh() >= 1000000) {
        throw new IllegalStateException("Cannot delete monetary value greater than 100000");
      } else {
        dao.deleteById(id);
      }
    }
    else {
    	throw new NullPointerException("Product ID not present");
    }
  }

  @Override
  public List<Product> findAll() {
    return dao.findAll();
  }

  @Override
  public Product findById(int id) {
    return dao.findById(id);
  }
}
