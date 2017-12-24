package org.flathub.api.dto;

import java.util.List;
import org.flathub.api.model.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

  List<CategoryDto> categoriesToCategoryDtos(List<Category> categories);
  CategoryDto categoryToCategoryDto(Category category);

}