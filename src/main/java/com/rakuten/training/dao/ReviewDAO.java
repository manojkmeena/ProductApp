package com.rakuten.training.dao;

import java.util.List;

import com.rakuten.training.domain.Review;

public interface ReviewDAO {

  Review findById(int id);

  Review save(Review toBeSaved);

  void deleteById(int id);

  List<Review> findAll();

  List<Review> findAllReviewsForProductID(int productId);
}