/*
 * Copyright 2007 The authors
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

package com.cfsoft.ofbiz.dom.screen.model;

import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.model.impl.DomModelFactory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Yann C&eacute;bron
 */
class ScreenModelFactory extends DomModelFactory<Screens, ScreensModel, PsiElement> {

  protected ScreenModelFactory(final Project project) {
    super(Screens.class, project, "ofbiz-screen");
  }

  protected List<ScreensModel> computeAllModels(@NotNull final Module module) {
    final PsiManager psiManager = PsiManager.getInstance(module.getProject());

     List<DomFileElement<Screens>> eles = DomService.getInstance().getFileElements(Screens.class, module.getProject(), GlobalSearchScope.projectScope(module.getProject()));
     final List<ScreensModel> models = new ArrayList<ScreensModel>();
      final Set<XmlFile> files = new LinkedHashSet<XmlFile>(eles.size());
      for (DomFileElement<Screens> ele : eles) {
          files.add(ele.getOriginalFile());
      }
      if (!files.isEmpty()) {
          final DomFileElement<Screens> element = createMergedModelRoot(files);
         final ScreensModel model;
        if (element != null) {
          model = new ScreensModelImpl(element, files);
          models.add(model);
        }
      }
      return models;


  }

  protected ScreensModel createCombinedModel(@NotNull final Set<XmlFile> xmlFiles,
                                            @NotNull final DomFileElement<Screens> screensDomFileElement,
                                            final ScreensModel screensModel,
                                            final Module module) {
    return new ScreensModelImpl(screensDomFileElement, xmlFiles);
  }

}