package com.cfsoft.ofbiz;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NonNls;

import javax.swing.*;


public class OfbizIcons {
    @NonNls
    private static final String ICON_BASE_PATH = "/resources/icons/";

    // "static" icons (DOM)
    //controller.xml
    public static final Icon CONTROLLER_CONFIG_FILE = IconLoader.findIcon("/resources/icons/controller.png");
    public static final Icon VIEW = IconLoader.findIcon("/resources/icons/action.png");
    public static final Icon SERVICE_CONFIG_FILE =IconLoader.findIcon("/resources/icons/service.png");
    public static final Icon SCREEN_CONFIG_FILE = IconLoader.findIcon("/resources/icons/ofbiz.png");
    public static final Icon SIMPLE_METHODS_FILE = IconLoader.findIcon("/resources/icons/presentation.png");
    public static final Icon COMPONENT_CONFIG_FILE = IconLoader.findIcon("/resources/icons/component.png");
    public static final Icon ENTITY_CONFIG_FILE = IconLoader.findIcon("/resources/icons/funnel.png");;
    public static final Icon RESOURCE_FILE = IconLoader.findIcon("/resources/icons/message.png");
    public static final Icon OFBIZ_VARIABLE =IconLoader.findIcon("/resources/icons/transform.png");
}
