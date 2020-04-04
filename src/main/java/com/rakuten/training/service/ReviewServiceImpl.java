package com.rakuten.training.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.dao.ProductDAO;
import com.rakuten.training.dao.ReviewDAO;
import com.rakuten.training.domain.Product;
import com.rakuten.training.domain.Review;

@Service
@Transactional
public class ReviewServiceImpl implements ReviewService {

  @Autowired 
  ReviewDAO dao;

  @Autowired 
  ProductDAO pdao;

  @Override
  public int addNewReview(Review toBeAdded, int productId) {
    Product prod = pdao.findById(productId);
    if (prod != null) {
      toBeAdded.setProduct(prod);
      Review added = dao.save(toBeAdded);
      return added.getId();
    } else {
    	throw new NoSuchProductException();
    }
  }

  @Override
  public void removeReview(int id) {
    Review existing = dao.findById(id);
    if (existing != null) {
      dao.deleteById(id);
    } else {
      throw new IllegalArgumentException("Review Does Not Exist");
    }
  }

  @Override
  public List<Review> findAll() {
    return dao.findAll();
  }

  @Override
  public Review findById(int id) {
    return dao.findById(id);
  }

  @Override
  public List<Review> findAllByProductId(int productId) {
	  return dao.findAllReviewsForProductID(productId);
  }
}
