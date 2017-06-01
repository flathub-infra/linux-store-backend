package org.flathub.api.util;

import org.flathub.api.model.App;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by jorge on 04/05/17.
 */
public class FlatpakRefFileCreator {


    public static void createFlatpakRefFile(String path, App app) throws IOException {

        //Use try-with-resource to get auto-closeable writer instance
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8)) {
            writer.write("[Flatpak Ref]" + "\n");
            writer.write("Title=" + app.getName() + "\n");
            writer.write("Name=" + app.getFlatpakAppId() + "\n");
            writer.write("Branch=" + app.getFlatpakRepo().getDefaultBranch() + "\n");
            writer.write("Url=" + app.getFlatpakRepo().getUrl() + "\n");
            writer.write("IsRuntime=false" + "\n");
            writer.write("GPGKey=" + app.getFlatpakRepo().getGpgkey() + "\n");
            writer.write("Homepage=" + app.getHomepageUrl() + "\n");
            writer.write("Comment=" + app.getSummary() + "\n");
            //writer.write("Icon=" + app.getIcon());
        }


    }

}
