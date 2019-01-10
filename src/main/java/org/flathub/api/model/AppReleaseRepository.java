package org.flathub.api.model;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by jorge on 01/01/19.
 */
@Repository
public interface AppReleaseRepository extends JpaRepository<AppRelease, Integer> {

  List<AppRelease> findAll();

  List<AppRelease> findByAppAndArch(App app, Arch arch);

  AppRelease findFirstByAppAndArchOrderByOstreeCommitDateDesc(App app, Arch arch);

  AppRelease findOneAppReleaseByAppAndArchAndOstreeCommitHash(App app, Arch arch, String commit);
}
