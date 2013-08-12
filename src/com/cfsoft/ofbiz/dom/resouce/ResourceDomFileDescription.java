package com.cfsoft.ofbiz.dom.resouce;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.dom.resouce.api.Resource;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class ResourceDomFileDescription extends DomFileDescription<Resource> {
    public ResourceDomFileDescription() {
        super(Resource.class, Resource.TAG_NAME);
    }

    @Override
    public Icon getFileIcon(final int flags) {
        return OfbizIcons.RESOURCE_FILE;
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
//        if (file.getName().equals(Constants.CONTROLLER_XML_DEFAULT_FILENAME) && file.getRootTag().getName().equals(Controller.TAG_NAME)) {
        if (file.getRootTag().getName().equals(Resource.TAG_NAME)) {
            return true;
        }
        return false;
    }

}
