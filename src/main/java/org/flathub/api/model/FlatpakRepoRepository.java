package org.flathub.api.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by jorge on 04/05/17.
 */
@Repository
public interface FlatpakRepoRepository extends JpaRepository<FlatpakRepo, Integer>{

    FlatpakRepo findOneByName(String name);
}
