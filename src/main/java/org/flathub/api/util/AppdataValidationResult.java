package org.flathub.api.util;

import java.util.ArrayList;

public class AppdataValidationResult {

  private boolean valid;
  private final ArrayList<String> issues = new ArrayList<>();


  public boolean isValid() {
    return valid;
  }

  public void setValid(boolean valid) {
    this.valid = valid;
  }

  public ArrayList<String> getIssues() {
    return issues;
  }
}

