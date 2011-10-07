package com.cfsoft.ofbiz.dom.component;

import com.cfsoft.ofbiz.OfbizUtils;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 9/27/11
 * Time: 8:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class ComponentUrl {
    private static final Pattern regex = Pattern.compile("\\s*component://(\\w+)/(([^#]+)((#)(.*))?)?\\s*");
    private static final String URL_FORMAT = "component://%s/%s%s";
    private String componentName;
    private String relativePath;
    private String tag;
    private boolean startTag;
    private final String url;
    private String baseName = null;
    private String relativeDirectory;

    public ComponentUrl(String url) {
        this.url = OfbizUtils.removeIdeaPostFix(url);
        try {
            Matcher regexMatcher = regex.matcher(url);
            if (regexMatcher.find()) {
                componentName = regexMatcher.group(1);
                relativePath = regexMatcher.group(3);
                if (relativePath != null) {
                    int pos = relativePath.lastIndexOf("/");
                    if (pos >= 0) {
                        baseName = relativePath.substring(pos);
                        relativeDirectory = relativePath.substring(0, relativePath.length() - baseName.length());
                    } else {
                        baseName = relativePath;
                        relativeDirectory = ".";
                    }
                }
                tag = regexMatcher.group(6);
                startTag = regexMatcher.group(5) != null;
            }
        } catch (PatternSyntaxException ex) {
            // Syntax error in the regular expression
        }
    }

    public String getComponentName() {
        return componentName;
    }

    public String getRelativePath() {
        return relativePath;
    }

    public static String getRelativePath(Component component, String fullpath) {
        if (!fullpath.startsWith(component.getDirectory())) {
            return "";
        }
        return fullpath.substring(component.getDirectory().length() + 1);
    }

    public String getTag() {
        return tag;
    }

    public boolean isComponentUrl() {
        return relativePath != null;
    }

    public boolean hasComponentName() {
        return componentName != null;
    }

    public boolean hasTag() {
        return tag != null;
    }

    public boolean isStartTag() {
        return startTag;
    }

    public String getUrl() {
        return url;
    }

    public String getRealPath(Project project, Component... components) {
        if (relativePath != null) {
            Component[] list = components.length == 0 ?
                    ComponentManager.getInstance(project).getAllComponents() : components;
            for (Component component : list) {
                if (component.getName().getValue().equals(componentName)) {
                    return String.format("%s/%s", component.getDirectory(), relativePath);
                }
            }
        }
        return null;
    }

    public String getRealDirectory(Project project, Component... components) {
        if (relativeDirectory != null) {
            Component[] list = components.length == 0 ?
                    ComponentManager.getInstance(project).getAllComponents() : components;
            for (Component component : list) {
                if (component.getName().getValue().equals(componentName)) {
                    return String.format("%s/%s", component.getDirectory(), relativeDirectory);
                }
            }
        }
        return null;
    }

    public String getComponentDirectory(Project project, Component... components) {
        Component[] list = components.length == 0 ?
                ComponentManager.getInstance(project).getAllComponents() : components;
        for (Component component : list) {
            if (component.getName().getValue().equals(componentName)) {
                return component.getDirectory();
            }
        }
        return null;
    }

    public String getBaseName() {
        return baseName;
    }

    public String getRelativeDirectory() {
        return relativeDirectory;
    }

    public static String buildComponentUrl(@NotNull Component component, @NotNull String fullpath, @Nullable String tag) {
        if (!fullpath.startsWith(component.getDirectory())) {
            return "";
        }
        return String.format(URL_FORMAT, component.getName().getValue(),
                fullpath.substring(component.getDirectory().length() + 1), tag == null ? "" : "#" + tag);
    }

}
