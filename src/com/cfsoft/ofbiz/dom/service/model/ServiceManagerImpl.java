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
import com.cfsoft.ofbiz.facet.OfbizFacet;
import com.cfsoft.ofbiz.facet.ui.OfbizFileSet;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.xml.DomManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Yann C&eacute;bron
 */
public class ServiceManagerImpl extends ServiceManager {

  private final ServiceModelFactory factory;

  public ServiceManagerImpl(final Project project) {
    factory = new ServiceModelFactory(project);
  }

  public boolean isServiceConfigFile(@NotNull final XmlFile file) {
    return DomManager.getDomManager(file.getProject()).getFileElement(file, Services.class) != null;
  }

  @Nullable
  public ServiceModel getModelByFile(@NotNull final XmlFile file) {
    return factory.getModelByConfigFile(file);
  }

  @NotNull
  public List<ServiceModel> getAllModels(@NotNull final Module module) {
    return factory.getAllModels(module);
  }

  @Nullable
  public ServiceModel getCombinedModel(@Nullable final Module module) {
    return factory.getCombinedModel(module);
  }


    @Override
    public Services getService(XmlFile xmlFile) {
        return factory.getDom(xmlFile);
    }

}
