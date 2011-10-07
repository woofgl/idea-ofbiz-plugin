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
public class ControllerManagerImpl extends ControllerManager {

  private final ControllerModelFactory myControllerModelFactory;

  public ControllerManagerImpl(final Project project) {
    myControllerModelFactory = new ControllerModelFactory(project);
  }

  public boolean isStruts2ConfigFile(@NotNull final XmlFile file) {
    return DomManager.getDomManager(file.getProject()).getFileElement(file, Controller.class) != null;
  }

  @Nullable
  public ControllerModel getModelByFile(@NotNull final XmlFile file) {
    return myControllerModelFactory.getModelByConfigFile(file);
  }

  @NotNull
  public List<ControllerModel> getAllModels(@NotNull final Module module) {
    return myControllerModelFactory.getAllModels(module);
  }

  @Nullable
  public ControllerModel getCombinedModel(@Nullable final Module module) {
    return myControllerModelFactory.getCombinedModel(module);
  }


    @Override
    public Controller getController(XmlFile xmlFile) {
        return myControllerModelFactory.getDom(xmlFile);
    }

}
