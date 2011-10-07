package com.cfsoft.ofbiz.gotosymbol;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.cfsoft.ofbiz.dom.controller.model.ControllerModel;
import com.cfsoft.ofbiz.dom.controller.model.ControllerManager;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.navigation.NavigationItem;
import com.intellij.openapi.module.Module;
import com.intellij.util.Processor;
import com.intellij.util.xml.model.gotosymbol.GoToSymbolProvider;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;


public class GoToViewMapSymbolProvider extends GoToSymbolProvider {

  protected boolean acceptModule(final Module module) {
    return OfbizFacet.getInstance(module) != null;
  }

  protected void addNames(@NotNull final Module module, final Set<String> result) {
    final ControllerModel controllerModel = ControllerManager.getInstance(module.getProject()).getCombinedModel(module);
    if (controllerModel == null) {
      return;
    }

    controllerModel.processViewMap(new Processor<ViewMap>() {
        public boolean process(final ViewMap action) {
            result.add(action.getName().getStringValue());
            return true;
        }
    });
  }

  protected void addItems(@NotNull final Module module, final String name, final List<NavigationItem> result) {
    final ControllerModel controllerModel = ControllerManager.getInstance(module.getProject()).getCombinedModel(module);
    if (controllerModel == null) {
      return;
    }

    final List<ViewMap> actions = controllerModel.findViewMapByName(name);
    for (final ViewMap action : actions) {
      final NavigationItem item = createNavigationItem(action.getXmlTag(),
                                                       action.getName().getStringValue() +
                                                       " [" + action.getPage() + "]",
                                                       OfbizIcons.VIEW);
      result.add(item);
    }
  }

}