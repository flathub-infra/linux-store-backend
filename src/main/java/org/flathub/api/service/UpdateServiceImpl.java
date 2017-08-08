package org.flathub.api.service;

import com.google.common.io.Files;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import javax.xml.bind.JAXBException;
import org.flathub.api.model.App;
import org.flathub.api.model.FlatpakRepo;
import org.flathub.api.util.FlatpakRefFileCreator;
import org.freedesktop.appstream.AppdataComponent;
import org.freedesktop.appstream.AppdataParser;
import org.freedesktop.appstream.appdata.Icon;
import org.rauschig.jarchivelib.ArchiveFormat;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;
import org.rauschig.jarchivelib.CompressionType;
import org.rauschig.jarchivelib.Compressor;
import org.rauschig.jarchivelib.CompressorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Created by jorge on 20/05/17.
 */
@SuppressWarnings("unused")
@Service
public class UpdateServiceImpl implements UpdateService {

  private static final Logger LOGGER = LoggerFactory.getLogger(UpdateServiceImpl.class);
  private static final String APPSTREAM_TYPE_DESKTOP = "desktop";
  private static final String ICON_HEIGHT_HIDPI = "128";
  private static final String ICON_HEIGHT_DEFAULT = "64";


  @SuppressWarnings("unused")
  @Autowired
  private ApiService apiService;
  @Value("${flathub.appstream-extractor-info}")
  private String flathubAppStreamExtractorInfoFile;
  @Value("${flathub.flatpakref.server-path}")
  private String flathubFlatpakRefServerPath;
  @Value("${flathub.icons.server-path}")
  private String flathubIconsServerPath;

  @Override
  public void updateFlathubInfo() {

    AppstreamUpdateInfo updateInfo;

    try {
      updateInfo = getAppStreamUpdateInfo();

      if (updateInfo.isUpdatesAvailable()) {
        updateRepoInfo(updateInfo);
      } else {
        LOGGER.info("No updates available");
      }

    } catch (IOException e) {
      e.printStackTrace();
    }

  }


  private AppstreamUpdateInfo getAppStreamUpdateInfo() throws IOException {

    File extractorInfo = new File(flathubAppStreamExtractorInfoFile);
    AppstreamUpdateInfo info = new AppstreamUpdateInfo();
    info.setUpdatesAvailable(false);

    if (extractorInfo.exists()) {

      String line;

      //Use try-with-resource to get auto-closeable reader instance
      try (BufferedReader bufferedReader = new BufferedReader(new FileReader(extractorInfo))) {

        // use the readLine method of the BufferedReader to read one line at a time.
        // the readLine method returns null when there is nothing else to read.
        while ((line = bufferedReader.readLine()) != null) {
          if (line.startsWith("REPO=")) {
            info.setRepoName(line.replace("REPO=", ""));
          } else if (line.startsWith("EXPORT_DATA=")) {
            info.setExportDataPath(line.replace("EXPORT_DATA=", ""));
          } else if (line.startsWith("COMMIT=")) {
            info.setCommit(line.replace("COMMIT=", ""));
          } else if (line.startsWith("DATE=")) {
            //TODO: set date
            //info.setD
          }

        }

        File exportDataFolder = new File(info.getExportDataPath());
        if (exportDataFolder.exists() && exportDataFolder.isDirectory()) {
          info.setUpdatesAvailable(true);
        }

      }

    }

    return info;
  }


  private void updateRepoInfo(AppstreamUpdateInfo appstreamInfo) {

    Objects.requireNonNull(appstreamInfo);
    Objects.requireNonNull(appstreamInfo.getRepoName());
    Objects.requireNonNull(appstreamInfo.getExportDataPath());

    FlatpakRepo repo;

    LOGGER.info("Updating repo info for " + appstreamInfo.getRepoName());

    repo = apiService.findRepoByName(appstreamInfo.getRepoName());
    if (repo == null) {
      addFlathubRepo();
      repo = apiService.findRepoByName(appstreamInfo.getRepoName());
    }

    Objects.requireNonNull(repo);
    boolean isNewApp = false;

    File exportDataFolder = new File(appstreamInfo.getExportDataPath());
    if (exportDataFolder.exists() && exportDataFolder.isDirectory()) {

      File appstreamFile = new File(
        appstreamInfo.getExportDataPath() + File.separator + "appstream.xml");

      try {
        App app;
        List<AppdataComponent> componentList = AppdataParser.parseAppdataFile(appstreamFile);

        for (AppdataComponent component : componentList) {

          if (APPSTREAM_TYPE_DESKTOP.equalsIgnoreCase(component.getType())) {

            app = apiService.findAppByFlatpakAppId(component.getFlatpakId());
            if (app == null) {
              app = new App();
              isNewApp = true;
            }

            System.out.println("--------------------------");
            System.out.println("Id:" + component.getId());
            System.out.println("FlatpakId:" + component.getFlatpakId());

            app.setFlatpakAppId(component.getFlatpakId());
            app.setName(component.findDefaultName());
            app.setSummary(component.findDefaultSummary());
            app.setDescription(component.findDefaultDescription());
            app.setProjectLicense(component.getProjectLicense());

            Icon icon = component.findIconByHeight(ICON_HEIGHT_HIDPI);
            if (icon == null) {
              icon = component.findIconByHeight(ICON_HEIGHT_DEFAULT);
            }
            if (icon != null) {

              String iconPath;

              if(icon.getValue().contains(icon.getHeight() + "x" + icon.getWidth())){
                iconPath = appstreamInfo.getExportDataPath() + File.separator + "icons" +
                  File.separator + icon.getValue();
              }
              else {
                iconPath = appstreamInfo.getExportDataPath() + File.separator + "icons" +
                  File.separator + icon.getHeight() + "x" + icon.getWidth() +
                  File.separator + icon.getValue();
              }

              File iconFile = new File(iconPath);

              try {

                if (iconFile.isFile()) {

                  File destIconPath = new File(
                    flathubIconsServerPath + File.separator + app.getFlatpakAppId() + ".png");

                  if (!destIconPath.exists()) {
                    java.nio.file.Files.copy(iconFile.toPath(), destIconPath.toPath());
                  }
                }
                else{
                  LOGGER.info("Icon for " + app.getFlatpakAppId() + " not available");
                }
              } catch (IOException e) {
                LOGGER.error("Error copying icon", e);
              }
            }
            else{
              LOGGER.info("Icon for " + app.getFlatpakAppId() + " not available");
            }

            //TODO:
            //Categories
            //Icons
            //Keywords (translatable)
            //kudos
            //Screenshots
            //Languages (percentage)
            //<bundle type="flatpak" runtime="org.gnome.Platform/x86_64/3.22" sdk="org.gnome.Sdk/x86_64/3.22">app/org.gnome.Weather/x86_64/stable</bundle>

            app.setFlatpakRepo(repo);
            apiService.updateApp(app);

            File destFlatpakRefFile = new File(flathubFlatpakRefServerPath + File.separator +
              app.getFlatpakAppId()  + ".flatpakref");

            if (!destFlatpakRefFile.exists()) {
              try {
                FlatpakRefFileCreator.createFlatpakRefFile(
                  destFlatpakRefFile.getPath(), app);
              } catch (IOException e) {
                e.printStackTrace();
              }
            }

          }

        }

        //Once imported delete the appdata folder
        //exportDataFolder.delete();

      } catch (JAXBException e) {
        e.printStackTrace();
      }


    }


  }


  private void addGnomeAppsRepo() {

    FlatpakRepo repo = new FlatpakRepo();

    repo.setName("gnome-apps");
    repo.setDescription("Gnome Stable Applications");
    repo.setUrl("http://sdk.gnome.org/repo-apps/");
    repo.setHomepageUrl("https://www.gnome.org/get-involved/");
    repo.setDefaultBranch("stable");
    repo.setGpgkey(
      "mQENBFUUCGcBCAC/K9WeV4xCaKr3NKRqPXeY5mpaXAJyasLqCtrDx92WUgbu0voWrhohNAKpqizod2dvzc/XTxm3rHyIxmNfdhz1gaGhynU75Qw4aJVcly2eghTIl++gfDtOvrOZo/VuAq30f32dMIgHQdRwEpgCwz7WyjpqZYltPAEcCNL4MTChAfiHJeeiQ5ibystNBW8W6Ymf7sO4m4g5+/aOxI54oCOzD9TwBAe+yXcJJWtc2rAhMCjtyPJzxd0ZVXqIzCe1xRvJ6Rq7YCiMbiM2DQFWXKnmYQbj4TGNMnwNdAajCdrcBWEMSbzq7EzuThIJRd8Ky4BkEe1St6tuqwFaMZz+F9eXABEBAAG0KEdub21lIFNESyAzLjE2IDxnbm9tZS1vcy1saXN0QGdub21lLm9yZz6JATgEEwECACIFAlUUCGcCGwMGCwkIBwMCBhUIAgkKCwQWAgMBAh4BAheAAAoJEArkz6VV0VKBa5cH/0vXa31YgEjNk78gGFXqnQxdD1WYA87OYxDi189l4lA802EFTF4wCBuZyDOqdd5BhS3Ab0cR778DmZXRUP2gwe+1zTJypU2JMnDpkwJ4NK1VP6/tE4SAPrznBtmb76BKaWBqUfZ9Wq1zg3ugvqkZB/Exq+usypIOwQVp1KL58TrjBRda0HvRctzkNhr0qYAtkfLFe0GvksBp4vBm8uGwAx7fw/HbhIjQ9pekTwvB+5GwDPO/tSip/1bQfCS+XJB8Ffa04HYPLGedalnWBrwhYY+G/kn5Zh9L/AC8xeLwTJTHM212rBjPa9CWs9C6a57MSaeGIEHLC1hEyiJJ15w8jmY=");

    apiService.updateFlatpakRepo(repo);
  }

  private void addKdeAppsRepo() {

    FlatpakRepo repo = new FlatpakRepo();

    repo.setName("kdeapps");
    repo.setDescription("KDE Testing Applications");
    repo.setUrl("http://distribute.kde.org/flatpak-apps-testing/");
    repo.setHomepageUrl("https://www.kde.org/");
    repo.setDefaultBranch("");
    repo.setGpgkey(
      "mQENBFdaDxkBCADFg5SnlJNKTa+qY4Zqhb89O0PQ/NPx/GCK/LRYu7+OAWqQljuKZ/GZUEWG1Fzz5GbOy+EH+hDYq6ReUAM958jsrz8pemmriAB/iQtwRXVXNQcJmeB8b4e6L96lcKTwZDYNPLO4CuGqPPkEF4CYzs+wjkDR6q6KiqBNelVTjTJUd3VBvQlbBhSGZeCXXnJOIYMf2XXETFv8tuoW3SO9dkC018O+xZgF2WIQjHCZK5uChkyF69i3eAtVPRDRoXetH6eI+2HTznhnv3liCu6b2AXCpu5IlnSeoDy2wgmtmYgwu0KzkivKVvaY7qQYSr0JWGioz9NSEsbrhtCb3G3+XuC1ABEBAAG0RUtERSBGbGF0cGFrIChUaGUgZS1tYWlsIGlzIGEgcHVibGljIG1haWxpbmcgbGlzdCkgPGtkZS1kZXZlbEBrZGUub3JnPokBNwQTAQgAIQUCV1oPGQIbAwULCQgHAgYVCAkKCwIEFgIDAQIeAQIXgAAKCRCOITqGYcRb7d2sCACu29H4jzC8bwDB3MMwYTy8nVfeJtCq1LPLnolFG0WMqDtLeOg2q3PdrjjJSquIuPbHTlq+1HWFrEJ3gJ+X26O8bw0acVWdMXPEuJiuQTd6RdWG1y6QpEqAlBVBQ1vF5vCdXrBed6nWodhQ1vQ0iPMzGh1dEyHI9wOyyF3+PCKhq6NY41cftoQSFeXtYgMUL82gq436gVvvqFockavDV407rZkmJflry+f9nNJrBTpNZijd0hi+eVQ0mty4dGoWPAI+1DcR8349vGHCHsCIRRGj0Dra89XKZXJZJDze7LutP7lcY7W2x0alZJogc++wxACos8NldyOvSLuXdrFaMKW4uQENBFdaDxkBCADCXBI4M1iOAcwuNUBeSl85s0VRzIalN2mlyIFw4401Z+heuUXYRrdSUokk+5ea4WiECxe8qw44kLEUVRTBar5xH2pUmmxjupBadzDhr8/Wa2WLj3O4DGPYRBK1A8zNhtL1safxczZ1EukCnIZzstp9gUBqVvAu5ebe3VcAoMYGuqltgxhOS41zDQ7hGyxx+NNvvhCBxjUOV9hmmCo2u0r4Vq28LXiRctEiCKYmgyDj1Hcq86Vlwp7sJ4V4m1Eyewq8IepMzz3zhMpnFnkd03NE5twP/puIwAArzmcLlUed0WOp0YffPqGQe5+NRIJyWkhaxj7BtK8WAVMPmPkW84u/ABEBAAGJAR8EGAEIAAkFAldaDxkCGwwACgkQjiE6hmHEW+3JxQf/bn24l++Nmjj+Vnzi9xZNPKU9DmAQTxigTTBSRkkTLBqjaJn1C0Wuiago7TDrIqGlvA0H1xSYSxiiAnauvxhTEO0o9WNAhbdLotMk3KrysuW/vE+ZRecDoi/2aYX0ANnRG4jDl/2yXYo0+iH9qADkTHYJyhT3U3MDJDjgAQC03jnYe+9Hc6N801eGw/sQvSjLEGsne+nEWwMmhpj8puVCLEoiSwd76fnhcaJuvOwgrldr0NZV83P5hOMc1ABVUIBfPXZbOqfT45HqEmLQi7K9U/slJgL2EZKHXeJ8xF8jsrLKn3gvQCquQrPPaLiiqvO5nmetFZ8+6m8OZMzax92WHg==");

    apiService.updateFlatpakRepo(repo);
  }

  private void addEOSAppsRepo() {

    FlatpakRepo repo = new FlatpakRepo();

    repo.setName("eos-apps");
    repo.setDescription("Endless Applications");
    repo.setUrl("https://ostree.endlessm.com/ostree/eos-apps");
    repo.setHomepageUrl("https://endlessos.com");
    repo.setDefaultBranch("");
    repo.setGpgkey(
      "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");

    apiService.updateFlatpakRepo(repo);

  }

  private void addFlathubRepo() {

    FlatpakRepo repo = new FlatpakRepo();

    repo.setName("flathub");
    repo.setDescription("Central repository of Flatpak applications");
    repo.setUrl("https://flathub.org/repo/");
    repo.setHomepageUrl("https://flathub.org/");
    repo.setDefaultBranch("stable");
    repo.setGpgkey(
      "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");

    apiService.updateFlatpakRepo(repo);

  }


  private String extractContainerAppDataTarGz(String appStreamTarGzFile) throws IOException {

    File archive = new File(appStreamTarGzFile);

    String destinationPath =
      "/home/jorge/appstream-extractor/temp/" + Files.getNameWithoutExtension(appStreamTarGzFile);
    File appdataXMLarchive = new File(destinationPath + File.separator + "appstream.xml.gz");

    // Extract the main tar.gz file
    File destination = new File(destinationPath);
    Archiver archiver = ArchiverFactory.createArchiver(ArchiveFormat.TAR, CompressionType.GZIP);
    archiver.extract(archive, destination);

    // Extract the file appstream.xml.gz
    Compressor compressor = CompressorFactory.createCompressor(CompressionType.GZIP);
    try {
      compressor.decompress(appdataXMLarchive, destination);
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    return destinationPath;
  }


  public class AppstreamUpdateInfo {

    boolean updatesAvailable;
    String repoName;
    String exportDataPath;
    String commit;
    LocalDateTime commitDate;

    public AppstreamUpdateInfo() {
    }

    public String getRepoName() {
      return repoName;
    }

    public void setRepoName(String repoName) {
      this.repoName = repoName;
    }

    public boolean isUpdatesAvailable() {
      return updatesAvailable;
    }

    public void setUpdatesAvailable(boolean updatesAvailable) {
      this.updatesAvailable = updatesAvailable;
    }

    public String getExportDataPath() {
      return exportDataPath;
    }

    public void setExportDataPath(String exportDataPath) {
      this.exportDataPath = exportDataPath;
    }

    public String getCommit() {
      return commit;
    }

    public void setCommit(String commit) {
      this.commit = commit;
    }

    public LocalDateTime getCommitDate() {
      return commitDate;
    }

    public void setCommitDate(LocalDateTime commitDate) {
      this.commitDate = commitDate;
    }
  }

}
