package org.flathub.api.service;

import org.flathub.api.model.Arch;
import org.flathub.api.model.FlatpakRefRemoteInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("OptionalGetWithoutIsPresent")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "DEV")
public class LocalFlatpakInstallationServiceImplTest {

  @Autowired
  private
  LocalFlatpakInstallationServiceImpl service;

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  @Test
  public void when_getQuickBasicRemoteInfoByArchAndId_Gedit_x86_64_Expect_SameBasicInfoAsGetRemoteInfoByArchAndId() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> basicInfo = service.getQuickBasicRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "org.gnome.gedit");
    Optional<FlatpakRefRemoteInfo> completeInfo = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "org.gnome.gedit");

    //Then
    assertThat(basicInfo.isPresent()).isTrue();
    assertThat(completeInfo.isPresent()).isTrue();

    assertThat(basicInfo.get().getRef()).isEqualToIgnoringCase(completeInfo.get().getRef());
    assertThat(basicInfo.get().getId()).isEqualToIgnoringCase(completeInfo.get().getId());
    assertThat(basicInfo.get().getArch()).isEqualToIgnoringCase(completeInfo.get().getArch());
    assertThat(basicInfo.get().getBranch()).isEqualToIgnoringCase(completeInfo.get().getBranch());
    assertThat(basicInfo.get().getCollection()).isNullOrEmpty();
    assertThat(basicInfo.get().getDate()).isNull();
    assertThat(basicInfo.get().getSubject()).isNullOrEmpty();
    assertThat(basicInfo.get().getCommit()).isNullOrEmpty();
    assertThat(basicInfo.get().getShortCommit()).isEqualToIgnoringCase(completeInfo.get().getShortCommit());
    assertThat(basicInfo.get().getParent()).isNullOrEmpty();
    assertThat(basicInfo.get().getDownloadSize()).isEqualToIgnoringCase(completeInfo.get().getDownloadSize());
    assertThat(basicInfo.get().getInstalledSize()).isEqualToIgnoringCase(completeInfo.get().getInstalledSize());
    assertThat(basicInfo.get().getRuntime()).isNullOrEmpty();
    assertThat(basicInfo.get().getSdk()).isNullOrEmpty();
    assertThat(basicInfo.get().isEndOfLife()).isFalse();
    assertThat(basicInfo.get().getEndOfLife()).isNullOrEmpty();
    assertThat(basicInfo.get().getEndOfLifeRebase()).isNullOrEmpty();


  }

  @Test
  public void when_getQuickBasicRemoteInfoOfEoldRef_Expect_CorrectEoldInfo() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getQuickBasicRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "com.github.bitseater.weather");

    //Then
    assertThat(info.isPresent()).isTrue();
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/com.github.bitseater.weather/x86_64/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("com.github.bitseater.weather");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.X86_64.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isNullOrEmpty();
    assertThat(info.get().getDate()).isNull();
    assertThat(info.get().getSubject()).isNullOrEmpty();
    assertThat(info.get().getCommit()).isNullOrEmpty();
    assertThat(info.get().getShortCommit()).isEqualToIgnoringCase("aaa4a1810027");
    assertThat(info.get().getParent()).isNullOrEmpty();
    assertThat(info.get().getDownloadSize()).isEqualToIgnoringCase("1,2 MB");
    assertThat(info.get().getInstalledSize()).isEqualToIgnoringCase("6,1 MB");
    assertThat(info.get().getRuntime()).isNullOrEmpty();
    assertThat(info.get().getSdk()).isNullOrEmpty();

    assertThat(info.get().isEndOfLife()).isTrue();
    assertThat(info.get().getEndOfLife()).isEqualToIgnoringCase("This application has been renamed to com.gitlab.bitseater.meteo");
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();

  }

  @Test
  public void when_getRemoteInfoOfEoldRef_Expect_CorrectEoldInfo() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "com.github.bitseater.weather");

    //Then
    assertThat(info.isPresent()).isTrue();
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/com.github.bitseater.weather/x86_64/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("com.github.bitseater.weather");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.X86_64.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isEqualToIgnoringCase("org.flathub.Stable");

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss Z");
    assertThat(info.get().getDate()).isEqualToComparingFieldByField(LocalDateTime.parse("2018-12-12 14:34:14 +0000", formatter));


    assertThat(info.get().getSubject()).isEqualToIgnoringCase("Update eol message (2f82d9d8)");
    assertThat(info.get().getCommit()).isEqualToIgnoringCase("aaa4a1810027de1b32e9e077288622de7824a6f25622359889a8192e21d86597");
    assertThat(info.get().getShortCommit()).isEqualToIgnoringCase("aaa4a1810027");
    assertThat(info.get().getParent()).isEqualToIgnoringCase("17b37809fe86abb44a84588829790059e74c9321ebfdf0f90225b57bcaa8c002");
    assertThat(info.get().getDownloadSize()).isEqualToIgnoringCase("1,2 MB");
    assertThat(info.get().getInstalledSize()).isEqualToIgnoringCase("6,1 MB");
    assertThat(info.get().getRuntime()).isEqualToIgnoringCase("org.gnome.Platform/x86_64/3.26");
    assertThat(info.get().getSdk()).isEqualToIgnoringCase("org.gnome.Sdk/x86_64/3.26");
    assertThat(info.get().isEndOfLife()).isTrue();
    assertThat(info.get().getEndOfLife()).isEqualToIgnoringCase("This application has been renamed to com.gitlab.bitseater.meteo");
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
  }





  @Test
  public void when_getRemoteMetatataByRemoteAndArchAndId_Expect_CorrectMetadataObtained() {

    //Given

    //When

    Optional<String> metadata  = service.getRemoteMetatataByRemoteAndArchAndId("flathub", Arch.X86_64, "com.github.bitseater.weather");


    //Then
    assertThat(metadata.isPresent());
    assertThat(metadata.get().equalsIgnoreCase("[Application]\n" +
      "name=com.github.bitseater.weather\n" +
      "runtime=org.gnome.Platform/x86_64/3.26\n" +
      "sdk=org.gnome.Sdk/x86_64/3.26\n" +
      "command=com.github.bitseater.weather\n" +
      "\n" +
      "[Context]\n" +
      "shared=network;ipc;\n" +
      "sockets=x11;wayland;\n" +
      "devices=dri;\n" +
      "filesystems=xdg-run/dconf;~/.config/dconf:ro;\n" +
      "\n" +
      "[Session Bus Policy]\n" +
      "org.kde.StatusNotifierWatcher=talk\n" +
      "ca.desrt.dconf=talk\n" +
      "\n" +
      "[System Bus Policy]\n" +
      "org.freedesktop.GeoClue2=talk\n" +
      "\n" +
      "[Environment]\n" +
      "DCONF_USER_CONFIG_DIR=.config/dconf\n" +
      "\n" +
      "[Extension com.github.bitseater.weather.Locale]\n" +
      "directory=share/runtime/locale\n" +
      "autodelete=true\n" +
      "locale-subset=true\n" +
      "\n" +
      "[Build]\n" +
      "built-extensions=com.github.bitseater.weather.Locale;com.github.bitseater.weather.Sources;\n"));



  }


  @Test
  public void when_getQuickBasicRemoteInfoOfAllEoldRefs_Expect_CorrectEoldInfo() {

    //Given

    //When

    List<FlatpakRefRemoteInfo> remoteInfoList  = service.getAllQuickBasicRemoteInfoByRemote("flathub");

    //Then
    assertThat(remoteInfoList).isNotEmpty();

    for(FlatpakRefRemoteInfo info : remoteInfoList){
      if(info.isEndOfLife()){
        assertThat(info.getEndOfLife()).isNotEmpty();
      }
    }

  }


  @Test
  public void when_getRemoteInfoByArchAndId_Gedit_x86_64_Expect_InfoObtained() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "org.gnome.gedit");

    //Then
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/org.gnome.gedit/x86_64/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("org.gnome.gedit");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.X86_64.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isEqualToIgnoringCase("org.flathub.Stable");
    assertThat(info.get().getDate()).isInstanceOf(LocalDateTime.class);
    assertThat(info.get().getDate()).isGreaterThan(LocalDateTime.of(2018, 1, 1, 0, 0,0));
    assertThat(info.get().getSubject()).isNotEmpty();
    assertThat(info.get().getCommit()).isNotEmpty();
    assertThat(info.get().getParent()).isNotEmpty();
    assertThat(info.get().getDownloadSize()).isNotEmpty();
    assertThat(info.get().getInstalledSize()).isNotEmpty();
    assertThat(info.get().getRuntime()).startsWith("org.gnome.Platform/x86_64/");
    assertThat(info.get().getSdk()).startsWith("org.gnome.Sdk/x86_64/");
    assertThat(info.get().isEndOfLife()).isFalse();
    assertThat(info.get().getEndOfLife()).isNullOrEmpty();
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
  }

  @Test
  public void when_getRemoteInfoByArchAndId_Gedit_i386_Expect_InfoObtained() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.I386, "org.gnome.gedit");

    //Then
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/org.gnome.gedit/i386/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("org.gnome.gedit");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.I386.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isEqualToIgnoringCase("org.flathub.Stable");
    assertThat(info.get().getDate()).isInstanceOf(LocalDateTime.class);
    assertThat(info.get().getDate()).isGreaterThan(LocalDateTime.of(2018, 1, 1, 0, 0,0));
    assertThat(info.get().getSubject()).isNotEmpty();
    assertThat(info.get().getCommit()).isNotEmpty();
    assertThat(info.get().getParent()).isNotEmpty();
    assertThat(info.get().getDownloadSize()).isNotEmpty();
    assertThat(info.get().getInstalledSize()).isNotEmpty();
    assertThat(info.get().getRuntime()).startsWith("org.gnome.Platform/i386/");
    assertThat(info.get().getSdk()).startsWith("org.gnome.Sdk/i386/");
    assertThat(info.get().isEndOfLife()).isFalse();
    assertThat(info.get().getEndOfLife()).isNullOrEmpty();
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
  }

  @Test
  public void when_getRemoteInfoByArchAndId_Gedit_arm_Expect_InfoObtained() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.ARM, "org.gnome.gedit");

    //Then
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/org.gnome.gedit/arm/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("org.gnome.gedit");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.ARM.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isEqualToIgnoringCase("org.flathub.Stable");
    assertThat(info.get().getDate()).isInstanceOf(LocalDateTime.class);
    assertThat(info.get().getDate()).isGreaterThan(LocalDateTime.of(2018, 1, 1, 0, 0,0));
    assertThat(info.get().getSubject()).isNotEmpty();
    assertThat(info.get().getCommit()).isNotEmpty();
    assertThat(info.get().getParent()).isNotEmpty();
    assertThat(info.get().getDownloadSize()).isNotEmpty();
    assertThat(info.get().getInstalledSize()).isNotEmpty();
    assertThat(info.get().getRuntime()).startsWith("org.gnome.Platform/arm/");
    assertThat(info.get().getSdk()).startsWith("org.gnome.Sdk/arm/");
    assertThat(info.get().isEndOfLife()).isFalse();
    assertThat(info.get().getEndOfLife()).isNullOrEmpty();
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
  }

  @Test
  public void when_getRemoteInfoByArchAndId_Gedit_aarch64_Expect_InfoObtained() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.AARCH64, "org.gnome.gedit");

    //Then
    assertThat(info.isPresent());
    assertThat(info.get().getRef()).isEqualToIgnoringCase("app/org.gnome.gedit/aarch64/stable");
    assertThat(info.get().getId()).isEqualToIgnoringCase("org.gnome.gedit");
    assertThat(info.get().getArch()).isEqualToIgnoringCase(Arch.AARCH64.toString());
    assertThat(info.get().getBranch()).isEqualToIgnoringCase("stable");
    assertThat(info.get().getCollection()).isEqualToIgnoringCase("org.flathub.Stable");
    assertThat(info.get().getDate()).isInstanceOf(LocalDateTime.class);
    assertThat(info.get().getDate()).isGreaterThan(LocalDateTime.of(2018, 1, 1, 0, 0,0));
    assertThat(info.get().getSubject()).isNotEmpty();
    assertThat(info.get().getCommit()).isNotEmpty();
    assertThat(info.get().getParent()).isNotEmpty();
    assertThat(info.get().getDownloadSize()).isNotEmpty();
    assertThat(info.get().getInstalledSize()).isNotEmpty();
    assertThat(info.get().getRuntime()).startsWith("org.gnome.Platform/aarch64/");
    assertThat(info.get().getSdk()).startsWith("org.gnome.Sdk/aarch64/");
    assertThat(info.get().isEndOfLife()).isFalse();
    assertThat(info.get().getEndOfLife()).isNullOrEmpty();
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
    assertThat(info.get().isEndOfLife()).isFalse();
    assertThat(info.get().getEndOfLife()).isNullOrEmpty();
    assertThat(info.get().getEndOfLifeRebase()).isNullOrEmpty();
  }


  @Test
  public void when_getRemoteInfoByArchAndId_AppNotInRepo_Expect_InfoNotObtained() {

    //Given

    //When
    Optional<FlatpakRefRemoteInfo> info = service.getRemoteInfoByRemoteAndArchAndId("flathub", Arch.X86_64, "org.gnome.NOTPRESENT");

    //Then
    assertThat(!info.isPresent());

  }


}
