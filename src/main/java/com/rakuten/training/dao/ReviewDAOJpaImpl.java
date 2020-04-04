package com.rakuten.training.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.domain.Review;

@Repository
@Transactional
public class ReviewDAOJpaImpl implements ReviewDAO {
  @Autowired EntityManager em;

  @Override
  public Review findById(int id) {
    return em.find(Review.class, id);
  }

  @Override
  public Review save(Review toBeSaved) {
    //Product p = em.find(Product.class, productId);
    //toBeSaved.setProduct(p);
    em.persist(toBeSaved);
    return toBeSaved;
  }

  @Override
  public void deleteById(int id) {
    Query query = em.createQuery("DELETE FROM Review as r WHERE r.id=:idParam");
    query.setParameter("idParam", id);
    query.executeUpdate();
    /*	Review r = em.find(Review.class, id);
    em.remove(r);*/
  }

  @Override
  public List<Review> findAll() {
    Query query = em.createQuery("SELECT r FROM Review as r");
    @SuppressWarnings("unchecked")
    List<Review> all = query.getResultList();
    return all;
  }

  @Override
  public List<Review> findAllReviewsForProductID(int productId) {
	  TypedQuery<Review> query = em.createQuery("SELECT r FROM Review as r WHERE r.product.id = :prodid", Review.class);
//	  Query query = em.createQuery("SELECT r FROM Review as r WHERE r.product.id = :prodid");
      query.setParameter("prodid", productId);
//      @SuppressWarnings("unchecked")
      return query.getResultList();
  }
}
