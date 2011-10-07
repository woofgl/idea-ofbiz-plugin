package com.cfsoft.ofbiz.dom.controller.api;

import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.cfsoft.ofbiz.dom.controller.model.ControllerModel;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.ConvertContext;
import com.intellij.util.xml.ResolvingConverter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Collections;
import java.util.List;


public class ResponseValueConverter extends ResolvingConverter<ViewMap> {
    public String toString(@Nullable final ViewMap viewMap, final ConvertContext context) {
        return viewMap != null ? viewMap.getName().getStringValue() : null;
    }

    public String getErrorMessage(@Nullable final String value, final ConvertContext context) {
        return "Cannot resolve view-map ''" + value + "''";
    }

    @NotNull
    public Collection<? extends ViewMap> getVariants(final ConvertContext context) {
        final  ControllerModel controllerModel = getControllerModel(context);
        if (controllerModel == null) {
            return Collections.emptyList();
        }

        return controllerModel.getAllViewMaps();
    }

    public ViewMap fromString(@Nullable @NonNls final String name, final ConvertContext context) {
        if (StringUtil.isEmpty(name)) {
            return null;
        }

        final  ControllerModel controllerModel = getControllerModel(context);
        if (controllerModel == null) {
            return null;
        }

        List<ViewMap> list = getController(context).getAllViewMaps();
        for (ViewMap viewMap : list) {
            if (viewMap.getName().getStringValue().equals(name)) {
                return viewMap;
            }
        }
        return null;

    }

    @Nullable
    public static ControllerModel getControllerModel(final ConvertContext context) {
        final XmlFile xmlFile = context.getFile();
        return ControllerManager.getInstance(xmlFile.getProject()).getModelByFile(xmlFile);
    }
    @Nullable
    public static Controller getController(final ConvertContext context) {
        final XmlFile xmlFile = context.getFile();
        return ControllerManager.getInstance(xmlFile.getProject()).getController(xmlFile);
    }
}