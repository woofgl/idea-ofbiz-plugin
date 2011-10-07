package com.cfsoft.ofbiz.dom.fieldtype;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.dom.fieldtype.api.FieldtypeModel;
import com.intellij.openapi.module.Module;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileDescription;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class FieldtypeModelDomFileDescription extends DomFileDescription<FieldtypeModel> {
    public FieldtypeModelDomFileDescription() {
        super(FieldtypeModel.class, FieldtypeModel.TAG_NAME);
    }

    protected void initializeFileDescription() {
       // registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, Constants.CONTROLLER_DTDS);
//        registerNamespacePolicy(Constants.CONTROLLER_NAMESPACE_KEY, ROOT_ELEMENT_MAPPER);
    }

    @Override
    public Icon getFileIcon(final int flags) {
        return OfbizIcons.ENTITY_CONFIG_FILE;
    }

    @Override
    public boolean isMyFile(@NotNull XmlFile file, @Nullable Module module) {
//        if (file.getName().equals(Constants.CONTROLLER_XML_DEFAULT_FILENAME) && file.getRootTag().getName().equals(Controller.TAG_NAME)) {
        if (file.getRootTag().getName().equals(FieldtypeModel.TAG_NAME)) {
            return true;
        }
        return false;
    }


}
