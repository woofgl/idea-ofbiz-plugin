package com.cfsoft.ofbiz.facet;

import com.cfsoft.ofbiz.OfbizIcons;
import com.intellij.facet.Facet;
import com.intellij.facet.FacetType;
import com.intellij.facet.autodetecting.FacetDetector;
import com.intellij.facet.autodetecting.FacetDetectorRegistry;
import com.intellij.facet.impl.autodetecting.FacetDetectorRegistryEx;
import com.intellij.j2ee.web.WebUtilImpl;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Collection;
import java.util.Iterator;


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


  public void registerDetectors(FacetDetectorRegistry<OfbizFacetConfiguration> facetDetectorRegistry) {
    FacetDetectorRegistryEx registry = (FacetDetectorRegistryEx)facetDetectorRegistry;

    registry.registerUniversalDetectorByFileNameAndRootTag("controller.xml", "site-conf", new OfbizFacetDetector(),null);
  }

  private static class OfbizFacetDetector extends FacetDetector<VirtualFile, OfbizFacetConfiguration>
  {
    private OfbizFacetDetector() {
      super();
    }

    public OfbizFacetConfiguration detectFacet(VirtualFile source, Collection<OfbizFacetConfiguration> existentFacetConfigurations)
    {
      Iterator iterator = existentFacetConfigurations.iterator();
      if (iterator.hasNext()) {
        return (OfbizFacetConfiguration)iterator.next();
      }

      return new OfbizFacetConfiguration();
    }
  }

}
