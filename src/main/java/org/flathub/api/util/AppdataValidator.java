package org.flathub.api.util;

import org.flathub.api.model.App;
import org.freedesktop.appstream.AppdataComponent;

public class AppdataValidator {

  //FIXME: define this just in one place
  private static final String APPSTREAM_TYPE_DESKTOP = "desktop";


  /**
   * Check if the component contains the required appdata:
   * - For all components: FlatpakId, Name, Summary
   * - For apps require also: Description, Icon
   * <p>
   * Check data size limits:
   * - description.length < App.APP_DESCRIPTION_LENGTH
   *
   * @return true if the component has the required info for its type and respects the size limits
   */
  public static AppdataValidationResult validateAppdataInformation(AppdataComponent component,
                                                                   String iconBaseRelativePath,
                                                                   short iconHeightDefault,
                                                                   short iconHeightHidpi) {

    AppdataValidationResult appdataValidationResult = new AppdataValidationResult();

    appdataValidationResult.setValid(true);

    if (component.getFlatpakId() == null || "".equalsIgnoreCase(component.getFlatpakId())) {
      appdataValidationResult.setValid(false);
      appdataValidationResult.getIssues().add("component.id is null or empty");
    }

    if (component.findDefaultName() == null || "".equalsIgnoreCase(component.findDefaultName())) {
      appdataValidationResult.setValid(false);
      appdataValidationResult.getIssues().add("component.name is null or empty");
    }

    if (component.findDefaultSummary() == null || "".equalsIgnoreCase(component.findDefaultSummary())) {
      appdataValidationResult.setValid(false);
      appdataValidationResult.getIssues().add("component.summary is null or empty");
    }

    if (APPSTREAM_TYPE_DESKTOP.equalsIgnoreCase(component.getType())) {

      if (component.findDefaultDescription() == null) {
        //Add as an issue but not critical (still valid)
        appdataValidationResult.getIssues().add("component.description is null or empty");
      } else {
        if (component.findDefaultDescription().length() > App.APP_DESCRIPTION_LENGTH) {
          appdataValidationResult.setValid(false);
          appdataValidationResult.getIssues().add("component.description is longuer than " + App.APP_DESCRIPTION_LENGTH);
        }
      }


      String hiDpiIconUrl = component.findIconUrl(iconBaseRelativePath, iconHeightHidpi);
      String defaultIconUrl = component.findIconUrl(iconBaseRelativePath, iconHeightDefault);

      if ("".equalsIgnoreCase(hiDpiIconUrl) && "".equalsIgnoreCase(defaultIconUrl)) {
        appdataValidationResult.setValid(false);
        appdataValidationResult.getIssues().add("component.icon is null");
      }

    }

    return appdataValidationResult;
  }

}
