package org.flathub.api.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jorge on 21/11/17.
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

  List<Category> findAllByOrderByName();

  Category findOneByCategoryId(String categoryId);

  Category findOneByName(String categoryName);
}
