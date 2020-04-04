package com.rakuten.training.service;

import java.util.List;

import com.rakuten.training.domain.Review;

public interface ReviewService {

  int addNewReview(Review toBeAdded, int productId);

  void removeReview(int id);

  List<Review> findAll();

  Review findById(int id);

  List<Review> findAllByProductId(int productId);
}
