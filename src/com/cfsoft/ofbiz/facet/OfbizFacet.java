package com.cfsoft.ofbiz.facet;

import com.intellij.facet.Facet;
import com.intellij.facet.FacetManager;
import com.intellij.facet.FacetType;
import com.intellij.facet.FacetTypeId;
import com.intellij.javaee.web.facet.WebFacet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.Disposer;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class OfbizFacet extends Facet<OfbizFacetConfiguration> {

  public static final FacetTypeId<OfbizFacet> FACET_TYPE_ID = new FacetTypeId<OfbizFacet>("ofbiz");

  public OfbizFacet(@NotNull final FacetType facetType,
                     @NotNull final Module module,
                     final String name,
                     @NotNull final OfbizFacetConfiguration configuration,
                     final Facet underlyingFacet) {
    super(facetType, module, name, configuration, underlyingFacet);
    Disposer.register(this, configuration);
  }

  /**
   * Gets the StrutsFacet for the given module.
   *
   * @param module Module to check.
   * @return Instance or <code>null</code> if none configured.
   */
  @Nullable
  public static OfbizFacet getInstance(@NotNull final Module module) {
    return FacetManager.getInstance(module).getFacetByType(FACET_TYPE_ID);
  }

  /**
   * Gets the StrutsFacet for the module containing the given PsiElement.
   *
   * @param element Element to check.
   * @return Instance or <code>null</code> if none configured.
   */
  @Nullable
  public static OfbizFacet getInstance(@NotNull final PsiElement element) {
    final Module module = ModuleUtil.findModuleForPsiElement(element);
    return module != null ? getInstance(module) : null;
  }

}
