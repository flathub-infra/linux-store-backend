package org.flathub.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by jorge on 20/11/17.
 */
@Entity
public class Category {

  private int categoryId;
  private String name;
  private Set<App> apps = new HashSet<>();

  @JsonIgnore
  @Id
  @SequenceGenerator(name = "category_category_id_seq",
    sequenceName = "category_category_id_seq",
    allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE,
    generator = "category_category_id_seq")
  @Column(name = "category_id", nullable = false)
  public int getCategoryId() {
    return categoryId;
  }

  @JsonProperty
  public void setCategoryId(int categoryId) {
    this.categoryId = categoryId;
  }

  @NaturalId
  @Column(name = "name", nullable = false, length = 256)
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonIgnore
  @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
  public Set<App> getApps() {
    return apps;
  }

  public void setApps(Set<App> apps) {
    this.apps = apps;
  }

  public Category() {
  }

  public Category(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Category)) return false;

    Category category = (Category) o;

    if (categoryId != category.categoryId) return false;
    return name.equals(category.name);
  }

  @Override
  public int hashCode() {
    int result = categoryId;
    result = 31 * result + name.hashCode();
    return result;
  }
}
