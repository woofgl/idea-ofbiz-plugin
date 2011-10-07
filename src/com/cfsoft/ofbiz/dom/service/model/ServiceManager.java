/*
 * Copyright 2010 The authors
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
import com.intellij.openapi.util.NotNullLazyKey;
import com.intellij.psi.xml.XmlFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Set;

/**
 * Project-service for accessing xml and various utility methods.
 *
 * @author Yann C&eacute;bron
 */
public abstract class ServiceManager {

  private static final NotNullLazyKey<ServiceManager, Project> INSTANCE_KEY = com.intellij.openapi.components.ServiceManager.createLazyKey(ServiceManager.class);

  public static ServiceManager getInstance(@NotNull final Project project) {
    return INSTANCE_KEY.getValue(project);
  }

  /**
   * Checks whether the given file is a valid <code>struts.xml</code> file.
   *
   * @param xmlFile File to check.
   *
   * @return <code>true</code> if yes, <code>false</code> otherwise.
   */
  public abstract boolean isServiceConfigFile(@NotNull XmlFile xmlFile);

  /**
   * Gets the model using the given file.
   *
   * @param file File to resolve context.
   *
   * @return <code>null</code> if no model available.
   *
   * @see com.intellij.util.xml.model.impl.DomModelFactory#getModelByConfigFile(com.intellij.psi.xml.XmlFile)
   */
  @Nullable
  public abstract ServiceModel getModelByFile(@NotNull final XmlFile file);

  /**
   * Gets all models.
   *
   * @param module Module.
   *
   * @return List of all models.
   *
   * @see com.intellij.util.xml.model.impl.DomModelFactory#getAllModels(com.intellij.openapi.util.UserDataHolder)
   */
  @NotNull
  public abstract List<ServiceModel> getAllModels(@NotNull Module module);

  /**
   * Gets the combined model.
   *
   * @param module Module.
   *
   * @return Combined model.
   *
   * @see com.intellij.util.xml.model.impl.DomModelFactory#getCombinedModel(com.intellij.openapi.util.UserDataHolder) 
   */
  @Nullable
  public abstract ServiceModel getCombinedModel(@Nullable final Module module);

  /**
   * Gets all configured filesets from current facet setup.
   *
   * @param module Module to get filesets for.
   *
   * @return All configured filesets (can be empty).
   */
//  @NotNull
//  public abstract Set<OfbizFileSet> getAllConfigFileSets(@NotNull final Module module);
  public abstract Services getService(XmlFile xmlFile);

}