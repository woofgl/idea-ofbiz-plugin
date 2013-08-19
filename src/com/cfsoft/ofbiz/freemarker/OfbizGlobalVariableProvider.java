/*
 * Copyright 2013 The authors
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cfsoft.ofbiz.freemarker;

import com.cfsoft.ofbiz.OfbizIcons;
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.intellij.freemarker.psi.FtlType;
import com.intellij.freemarker.psi.files.FtlFile;
import com.intellij.freemarker.psi.files.FtlGlobalVariableProvider;
import com.intellij.freemarker.psi.files.FtlXmlNamespaceType;
import com.intellij.freemarker.psi.variables.FtlLightVariable;
import com.intellij.freemarker.psi.variables.FtlVariable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtilCore;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlDocument;
import com.intellij.psi.xml.XmlFile;
import com.intellij.xml.XmlNSDescriptor;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author peter
 */
public class OfbizGlobalVariableProvider extends FtlGlobalVariableProvider {

  @NotNull
  public List<? extends FtlVariable> getGlobalVariables(final FtlFile file) {
    final Module module = ModuleUtilCore.findModuleForPsiElement(file);
    if (module == null) {
      return Collections.emptyList();
    }

    if (OfbizFacet.getInstance(module) == null) {
      return Collections.emptyList();
    }

    final List<FtlVariable> result = new ArrayList<FtlVariable>();
    result.add(new MyFtlLightVariable("delegator", file, "org.ofbiz.entity.Delegator"));
    result.add(new MyFtlLightVariable("dispatcher", file, "org.ofbiz.service.LocalDispatcher"));
    result.add(new MyFtlLightVariable("authz", file, "org.ofbiz.security.authz.Authorization"));
    result.add(new MyFtlLightVariable("security", file, "org.ofbiz.security.Security"));
    result.add(new MyFtlLightVariable("userLogin", file, "org.ofbiz.entity.GenericValue"));
    result.add(new MyFtlLightVariable("response", file, "javax.servlet.http.HttpServletResponse"));
    result.add(new MyFtlLightVariable("application", file, "javax.servlet.ServletContext"));
    result.add(new MyFtlLightVariable("session", file, "javax.servlet.http.HttpSession"));
    result.add(new MyFtlLightVariable("request", file, "javax.servlet.http.HttpServletRequest"));
    result.add(new MyFtlLightVariable("sessionAttributes", file, "freemarker.ext.servlet.HttpSessionHashModel"));
    result.add(new MyFtlLightVariable("requestParameters", file, "java.util.Map"));
    result.add(new MyFtlLightVariable("<@ofbizUrl></@ofbizUrl>", file, "java.lang.String"));
    result.add(new MyFtlLightVariable("<@ofbizContentUrll></@ofbizContentUrl>", file, "java.lang.String"));
    installTaglibSupport(result, module);
    return result;
  }

  private static void installTaglibSupport(@NotNull final List<FtlVariable> result,
                                           @NotNull final Module module) {
    //final XmlFile xmlFile = JspManager.getInstance(module.getProject()).getTldFileByUri(taglibUri, module, null);
/*      Collection<XmlFile> xmlFiles = JspManager.getInstance(module.getProject()).getPossibleTldFiles(module);
      final XmlFile xmlFile = ContainerUtil.find(xmlFiles, new Condition<XmlFile>() {
          @Override
          public boolean value(XmlFile xmlFile) {
              System.out.println(xmlFile.getName());
              return xmlFile.getName().equals("ofbiz.tld");
          }
      });*/
      PsiFile[] files = FilenameIndex.getFilesByName(module.getProject(), "ofbiz.tld", GlobalSearchScope.allScope(module.getProject()));
    if (files.length == 0) {
      return;
    }

      XmlFile xmlFile = (XmlFile) files[0];

    final XmlDocument document = xmlFile.getDocument();
    if (document == null) {
      return;
    }

    final XmlNSDescriptor descriptor = (XmlNSDescriptor) document.getMetaData();
    if (descriptor == null) {
      return;
    }

    PsiElement declaration = descriptor.getDeclaration();
    if (declaration == null) {
      declaration = xmlFile;
    }

    result.add(new MyFtlLightVariable("ofbiz", declaration, new FtlXmlNamespaceType(descriptor)));
  }




  private static class MyFtlLightVariable extends FtlLightVariable {

    private MyFtlLightVariable(@NotNull @NonNls final String name,
                               @NotNull final PsiElement parent,
                               @Nullable final FtlType type) {
      super(name, parent, type);
    }

    private MyFtlLightVariable(@NotNull @NonNls final String name,
                               @NotNull final PsiElement parent,
                               @NotNull @NonNls final String psiType) {
      super(name, parent, psiType);
    }

    @Override
    public Icon getIcon(final boolean open) {
      return OfbizIcons.OFBIZ_VARIABLE;
    }
  }

}