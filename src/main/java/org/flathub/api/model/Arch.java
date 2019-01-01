package org.flathub.api.model;

@SuppressWarnings("SpellCheckingInspection")
public enum Arch {
  X86_64,
  I386,
  ARM,
  AARCH64;

  @Override
  public String toString() {
    switch(this) {
      case X86_64: return "x86_64";
      case I386: return "i386";
      case ARM: return "arm";
      case AARCH64: return "aarch64";
      default: throw new IllegalArgumentException();
    }
  }

}