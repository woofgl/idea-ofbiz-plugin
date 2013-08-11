package com.cfsoft.ofbiz.facet;

import com.cfsoft.ofbiz.OfbizIcons;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;


public class OfbizFacetType extends FacetType<OfbizFacet, OfbizFacetConfiguration> {
  OfbizFacetType() {
    super(OfbizFacet.FACET_TYPE_ID, "Ofbiz", "Ofbiz");
  }

  public static FacetType<OfbizFacet, OfbizFacetConfiguration> getInstance() {
    return findInstance(OfbizFacetType.class);
  }

  public OfbizFacetConfiguration createDefaultConfiguration() {
    return new OfbizFacetConfiguration();
  }

  public OfbizFacet createFacet(@NotNull final Module module,
                                 final String name,
                                 @NotNull final OfbizFacetConfiguration configuration,
                                 @Nullable final Facet underlyingFacet) {
    return new OfbizFacet(this, module, name, configuration, underlyingFacet);
  }

  public boolean isSuitableModuleType(final ModuleType moduleType) {
    return moduleType instanceof JavaModuleType;
  }

  public Icon getIcon() {
    return OfbizIcons.CONTROLLER_CONFIG_FILE;
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.project.structure.facets.ofbiz.facet";
  }
}
