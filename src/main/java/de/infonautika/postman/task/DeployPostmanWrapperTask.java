package de.infonautika.postman.task;

import de.infonautika.postman.PostmanExtension;
import org.apache.commons.io.FileUtils;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.OutputFile;
import org.gradle.api.tasks.TaskAction;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import static de.infonautika.postman.PostmanRunnerPlugin.GROUP_NAME;

public class DeployPostmanWrapperTask extends DefaultTask {
    public final static String NAME = "deployWrapper";

    public DeployPostmanWrapperTask() {
        setGroup(GROUP_NAME);
        setDescription("executes Postman collections");
    }

    @TaskAction
    public void createNewmanWrapper() {
        try {
            FileUtils.copyURLToFile(getInternalWrapperUrl(), getWrapperAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public URL getInternalWrapperUrl() {
        URL wrapperScriptResource = this.getClass().getResource(getConfig().getWrapperName());
        if (wrapperScriptResource == null) {
            throw new RuntimeException("could not get wrapper script resource");
        }
        return wrapperScriptResource;
    }

    @OutputFile
    public File getWrapperAbsolutePath() {
        return new File(getProject().getProjectDir(), getConfig().getWrapperRelativePath());
    }

    private PostmanExtension getConfig() {
        return getProject().getExtensions().getByType(PostmanExtension.class);
    }
}
