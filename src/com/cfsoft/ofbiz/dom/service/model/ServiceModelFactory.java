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

package com.cfsoft.ofbiz.dom.service.model;

import com.cfsoft.ofbiz.dom.service.api.Services;
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
import com.intellij.util.Function;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomManager;
import com.intellij.util.xml.DomService;
import com.intellij.util.xml.model.impl.DomModelFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.*;

/**
 * @author Yann C&eacute;bron
 */
class ServiceModelFactory extends DomModelFactory<Services, ServiceModel, PsiElement> {

    protected ServiceModelFactory(final Project project) {
        super(Services.class, project, "ofbiz-service");
    }

    protected List<ServiceModel> computeAllModels(@NotNull final Module module) {
        List<ServiceModel> models = new ArrayList<ServiceModel>();
        List<DomFileElement<Services>> eles = DomService.getInstance().getFileElements(Services.class, module.getProject(), GlobalSearchScope.projectScope(module.getProject()));
        final Set<XmlFile> files = new HashSet<XmlFile>();
                ContainerUtil.map(eles, new Function<DomFileElement<Services>, XmlFile>() {
            @Override
            public XmlFile fun(DomFileElement<Services> servicesDomFileElement) {
                files.add(servicesDomFileElement.getFile());
                return null;
            }
        });
        if (!files.isEmpty()) {
            final DomFileElement<Services> element = createMergedModelRoot(files);
            final ServiceModelImpl model;
            if (element != null) {
                model = new ServiceModelImpl(element, files);
                models.add(model);
            }
        }

        return models;
    }

    protected ServiceModel createCombinedModel(@NotNull final Set<XmlFile> xmlFiles,
                                               @NotNull final DomFileElement<Services> strutsRootDomFileElement,
                                               final ServiceModel strutsModel,
                                               final Module module) {
        return new ServiceModelImpl(strutsRootDomFileElement, xmlFiles);
    }

}