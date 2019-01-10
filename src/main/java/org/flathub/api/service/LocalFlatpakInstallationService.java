package org.flathub.api.service;

import org.flathub.api.model.Arch;
import org.flathub.api.model.FlatpakRefRemoteInfo;

import java.util.List;
import java.util.Optional;

public interface LocalFlatpakInstallationService {

  Optional<FlatpakRefRemoteInfo> getQuickBasicRemoteInfoByRemoteAndArchAndId(String remote, Arch arch, String id);

  List<FlatpakRefRemoteInfo> getAllQuickBasicRemoteInfoByRemote(String remote);

  Optional<FlatpakRefRemoteInfo> getRemoteInfoByRemoteAndArchAndId(String remote, Arch arch, String id, boolean retryIfFailed);

  Optional<FlatpakRefRemoteInfo> getRemoteInfoByRemoteAndArchAndId(String remote, Arch arch, String id);

  Optional<String> getRemoteMetatataByRemoteAndArchAndId(String remote, Arch arch, String id);
}
