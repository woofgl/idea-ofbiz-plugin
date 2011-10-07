package com.cfsoft.ofbiz;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.model.ControllerModel;
import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.ide.IconProvider;
import com.intellij.ide.presentation.PresentationIconProvider;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.DumbAware;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;


public class OfbizDomIconProvider extends IconProvider implements DumbAware, PresentationIconProvider {
    @Override
    public Icon getIcon(@NotNull PsiElement element, int flags) {
        if (element == null)
            throw new IllegalArgumentException("Argument 0 for @NotNull parameter of com/intellij/struts2/Struts2IconProvider.getIcon must not be null");
        if (!(element instanceof PsiClass)) {
            return null;
        }

        if ((!element.isPhysical()) || (!element.isValid())) {
            return null;
        }

        OfbizFacet strutsFacet = OfbizFacet.getInstance(element);
        if (strutsFacet == null) {
            return null;
        }

        PsiClass psiClass = (PsiClass) element;
        Module module = ModuleUtil.findModuleForPsiElement(psiClass);
        ControllerModel strutsModel = ControllerManager.getInstance(psiClass.getProject()).getCombinedModel(module);
        if ((strutsModel == null) || (!strutsModel.isServiceClass(psiClass))) {
            return null;
        }

//        LayeredIcon layeredIcon = new LayeredIcon(2);
//        Icon original = PsiClassImplUtil.getClassIcon(flags, psiClass);
//        layeredIcon.setIcon(original, 0);
//        layeredIcon.setIcon(StrutsIcons.ACTION_SMALL, 1, 0, 6);
//        return layeredIcon;
        return OfbizIcons.CONTROLLER_CONFIG_FILE;
    }

    public Icon getIcon(Object o, int i) {
        if (o instanceof Controller) {
            return OfbizIcons.CONTROLLER_CONFIG_FILE;
        }

        return null;
    }
}
