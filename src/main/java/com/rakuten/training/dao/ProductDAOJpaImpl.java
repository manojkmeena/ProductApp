package com.rakuten.training.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.rakuten.training.domain.Product;

@Repository
@Transactional
public class ProductDAOJpaImpl implements ProductDAO {
  @Autowired EntityManager em;

  @Override
  public Product save(Product toBeSaved) {
    em.persist(toBeSaved);
    return toBeSaved;
  }

  @Override
  public Product findById(int id) {
    return em.find(Product.class, id);
  }

  @SuppressWarnings("unchecked")
  @Override
  public List<Product> findAll() {
    Query query = em.createQuery("SELECT p FROM Product as p");
    List<Product> all = query.getResultList();
    return all;
  }

  @Override
  public void deleteById(int id) {
    /*Query query = em.createQuery("DELETE FROM Product as p WHERE p.id=:idParam");
    query.setParameter("idParam", id);
    query.executeUpdate();*/
    //Product p = em.find(Product.class, id);
	Product p = em.getReference(Product.class, id); // this would avoid a SELECT as opposed to find(), thus it is more optimised
    em.remove(p);
  }
}
