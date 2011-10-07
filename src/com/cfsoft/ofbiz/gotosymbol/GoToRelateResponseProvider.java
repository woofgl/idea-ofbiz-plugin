package com.cfsoft.ofbiz.gotosymbol;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.api.RequestMap;
import com.cfsoft.ofbiz.dom.controller.api.Response;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.intellij.codeInsight.navigation.DomGotoRelatedItem;
import com.intellij.navigation.GotoRelatedItem;
import com.intellij.navigation.GotoRelatedProvider;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.xml.*;
import com.intellij.util.Processor;
import com.intellij.util.xml.DomElement;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


public class GoToRelateResponseProvider extends GotoRelatedProvider {

    @NotNull
    @Override
    public List<? extends GotoRelatedItem> getItems(@NotNull final PsiElement psiElement) {

        if (!(psiElement instanceof XmlToken)) {
            return Collections.emptyList();
        }
        XmlElement ele = (XmlElement) psiElement.getContext();
        while (!(ele instanceof XmlTag)) {
            ele = (XmlElement) ele.getParent();
        }
        DomElement domEle = DomManager.getDomManager(ele.getProject()).getDomElement((XmlTag) ele);

        if (!(domEle instanceof ViewMap)) {
            return Collections.emptyList();
        }
        final ViewMap viewMap = (ViewMap) domEle;
        final PsiFile containingFile = psiElement.getContainingFile().getOriginalFile();

        final ControllerManager controllerManager = ControllerManager.getInstance(psiElement.getProject());
        final Controller controller = controllerManager.getController((XmlFile) containingFile);
        if (controller == null) {
            return Collections.emptyList();
        }
        Set<Controller> sets = controller.getAllIncludeBy();
        final List<GotoRelatedItem> items = new ArrayList<GotoRelatedItem>();
        sets.add(controller);
        List<Response> responses = new ArrayList<Response>();
        String viewName = (viewMap.getName().getStringValue());

        for (Controller c : sets) {
            for (RequestMap requestMap : c.getRequestMaps()) {
                for (Response response : requestMap.getResponse()) {
                    if (response.getViewName().getValue() != null && response.getViewName().getStringValue().equals(viewName)) {
                        responses.add(response);
                    }
                }
            }
        }

        for (Response response : responses) {
            items.add(new DomGotoRelatedItem(response.getParent()));
        }
        return items;
    }

    public boolean processResponses(Controller controller, Processor<Response> processor) {
        for (final RequestMap view : controller.getRequestMaps()) {
            for (Response response : view.getResponse()) {
                processor.process(response);
            }
        }
        return true;
    }

}