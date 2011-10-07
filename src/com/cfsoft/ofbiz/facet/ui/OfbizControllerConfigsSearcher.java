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
package com.cfsoft.ofbiz.facet.ui;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.vfs.JarFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.xml.DomFileElement;
import com.intellij.util.xml.DomService;

import java.util.List;

/**
 * @author Yann C&eacute;bron
 */
public class OfbizControllerConfigsSearcher implements OfbizConfigsSercher {

    private final Module module;
    private final MultiMap<Module, PsiFile> myFiles = new MultiMap<Module, PsiFile>();
    private final MultiMap<VirtualFile, PsiFile> myJars = new MultiMap<VirtualFile, PsiFile>();

    public OfbizControllerConfigsSearcher(final Module module) {
        this.module = module;
    }

    @Override
    public void search() {
        myFiles.clear();
        myJars.clear();

        final List<DomFileElement<Controller>> elements =
//      DomService.getInstance().getFileElements(Controller.class, module.getProject(), GlobalSearchScope.moduleWithDependenciesScope(module));
        DomService.getInstance().getFileElements(Controller.class, module.getProject(), GlobalSearchScope.allScope(module.getProject()));
        for (final DomFileElement<Controller> element : elements) {
            final XmlFile file = element.getFile();
            final Module module = ModuleUtil.findModuleForPsiElement(file);
            if (module != null) {
                myFiles.putValue(module, file);
            }
        }

    }

    @Override
    public MultiMap<Module, PsiFile> getFilesByModules() {
        return myFiles;
    }

    @Override
    public MultiMap<VirtualFile, PsiFile> getJars() {
        return myJars;
    }

}