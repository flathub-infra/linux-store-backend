package org.flathub.api.model;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Created by jorge on 24/03/17.
 */
@Repository
public interface AppRepository extends JpaRepository<App, Integer> {


  List<App> findAll(Sort sort);

  List<App> findByCategories_Name(String categoryName, Sort sort);

  @Query(value="select * "
    + "from public.app "
    + "where "
    + "(current_release_date > current_date - interval '7' day  and current_release_date < current_date + interval '1' day) "
    + "or "
    + "(in_store_since_date > current_date - interval '7' day  and in_store_since_date < current_date + interval '1' day) "
    + "order by GREATEST(coalesce(in_store_since_date, '1900-01-01 00:00:00'), "
    + "                  coalesce(current_release_date, '1900-01-01 00:00:00')) desc",
    nativeQuery = true)
  List<App> findRecentlyAddedOrUpdated();

  App findOneByFlatpakAppId(String flatpakAppId);


}
