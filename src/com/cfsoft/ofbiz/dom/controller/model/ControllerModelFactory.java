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

package com.cfsoft.ofbiz.dom.controller.model;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.facet.ui.OfbizFileSet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.model.impl.DomModelFactory;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Yann C&eacute;bron
 */
class ControllerModelFactory extends DomModelFactory<Controller, ControllerModel, PsiElement> {

  protected ControllerModelFactory(final Project project) {
    super(Controller.class, project, "ofbiz-controller");
  }

  protected List<ControllerModel> computeAllModels(@NotNull final Module module) {
    final Set<XmlFile> files = new HashSet<XmlFile>();
      List<DomFileElement<Controller>> eles = DomService.getInstance().getFileElements(Controller.class,
              module.getProject(), GlobalSearchScope.projectScope(module.getProject()));
      for (DomFileElement<Controller> ele : eles) {
          files.add(ele.getFile());
      }

    final List<ControllerModel> models = new ArrayList<ControllerModel>();

      if (!files.isEmpty()) {
        final DomFileElement<Controller> element = createMergedModelRoot(files);
        final ControllerModelImpl model;
        if (element != null) {
          model = new ControllerModelImpl(element, files);
          models.add(model);
        }
      }


    return models;
  }

  protected ControllerModel createCombinedModel(@NotNull final Set<XmlFile> xmlFiles,
                                            @NotNull final DomFileElement<Controller> strutsRootDomFileElement,
                                            final ControllerModel strutsModel,
                                            final Module module) {
    return new ControllerModelImpl(strutsRootDomFileElement, xmlFiles);
  }

}