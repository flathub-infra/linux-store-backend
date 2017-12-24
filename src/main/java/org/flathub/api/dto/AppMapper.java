package org.flathub.api.dto;

import org.flathub.api.model.App;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * Created by jorge on 18/12/17.
 */
@Mapper(componentModel = "spring", uses = CategoryMapper.class)
public interface AppMapper {

  List<AppDto> appsToAppDtos(List<App> apps);
  AppDto appToAppDto(App app);

  List<AppFullDto> appsToAppFullDtos(List<App> apps);
  AppFullDto appToAppFullDto(App app);

}
