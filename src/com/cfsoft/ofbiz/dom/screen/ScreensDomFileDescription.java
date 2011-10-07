package com.cfsoft.ofbiz.dom.screen;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.NotNullFunction;
import com.intellij.util.xml.DomFileDescription;
import com.intellij.util.xml.DomFileElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class ScreensDomFileDescription extends DomFileDescription<Screens> {
    public ScreensDomFileDescription() {
        super(Screens.class, Screens.TAG_NAME);
    }

    protected void initializeFileDescription() {
       // registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, Constants.CONTROLLER_DTDS);
//        registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, ROOT_ELEMENT_MAPPER);
    }

    @Override
    public Icon getFileIcon(final int flags) {
        return OfbizIcons.SCREEN_CONFIG_FILE;
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
//        if (file.getName().equals(Constants.CONTROLLER_XML_DEFAULT_FILENAME) && file.getRootTag().getName().equals(Controller.TAG_NAME)) {
        if (file.getRootTag().getName().equals(Screens.TAG_NAME)) {
            return true;
        }
        return false;
    }





    private static final NotNullFunction<DomFileElement<Controller>, Controller> ROOT_ELEMENT_MAPPER =
            new NotNullFunction<DomFileElement<Controller>, Controller>() {
                @NotNull
                public Controller fun(final DomFileElement<Controller> strutsRootDomFileElement) {
                    return strutsRootDomFileElement.getRootElement();
                }
            };
}
