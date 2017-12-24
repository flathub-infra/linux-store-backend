package org.flathub.api.service;

import org.flathub.api.model.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Created by jorge on 17/12/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "TEST")
public class ApiServiceImplTest {


  @Autowired
  ApiService service;

  @Test
  public void findAllApps() throws Exception {

    //Given

    //When
    List<App> list = service.findAllApps();

    //Then
    assertThat(list).isNotEmpty();

  }

  @Test
  public void findAllAppsByCategoryName() throws Exception {
  }

  @Test
  public void findAppByFlatpakAppId() throws Exception {
  }

}