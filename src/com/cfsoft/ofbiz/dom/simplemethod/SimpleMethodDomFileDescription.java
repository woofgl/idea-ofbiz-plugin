package com.cfsoft.ofbiz.dom.simplemethod;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.dom.simplemethod.api.SimpleMethods;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class SimpleMethodDomFileDescription extends DomFileDescription<SimpleMethods> {
    public SimpleMethodDomFileDescription() {
        super(SimpleMethods.class, SimpleMethods.TAG_NAME);
    }

    protected void initializeFileDescription() {
       // registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, Constants.CONTROLLER_DTDS);
//        registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, ROOT_ELEMENT_MAPPER);
    }

    @Override
    public Icon getFileIcon(final int flags) {
        return OfbizIcons.SIMPLE_METHODS_FILE;
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
//        if (file.getName().equals(Constants.CONTROLLER_XML_DEFAULT_FILENAME) && file.getRootTag().getName().equals(Controller.TAG_NAME)) {
        if (file.getRootTag().getName().equals(SimpleMethods.TAG_NAME)) {
            return true;
        }
        return false;
    }

}
