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

package com.cfsoft.ofbiz.facet.ui;

import com.cfsoft.ofbiz.facet.OfbizFacetConfiguration;
import com.intellij.facet.Facet;
import com.intellij.facet.frameworks.LibrariesDownloadAssistant;
import com.intellij.facet.frameworks.beans.Artifact;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.libraries.LibraryInfo;
import com.intellij.openapi.module.Module;
import com.intellij.util.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Struts2 facet tab "Features".
 *
 * @author Yann C&eacute;bron
 */
public class DatabaseConfigurationTab extends FacetEditorTab {

  private JPanel myPanel;
  private JComboBox databaseComboBox;

  private final OfbizFacetConfiguration originalConfiguration;

  public DatabaseConfigurationTab(final OfbizFacetConfiguration originalConfiguration,
                                  final FacetEditorContext editorContext) {
    this.originalConfiguration = originalConfiguration;

    final Module module = editorContext.getModule();
    //final String version = OfbizVersionDetector.detectStrutsVersion(module);
    final String[] databases = {"mysql","oracle","sybase","derby","mssql"};
      databaseComboBox.setModel(new DefaultComboBoxModel(databases));
      databaseComboBox.getModel().setSelectedItem(databases);
//      databaseComboBox.setEnabled(false);
      return;
  }


  public void onFacetInitialized(@NotNull final Facet facet) {

  }

  @Nullable
  public Icon getIcon() {
    return Icons.TASK_ICON;
  }

  @Nls
  public String getDisplayName() {
    return "Database Config";
  }

  public JComponent createComponent() {
    return myPanel;
  }

  public boolean isModified() {
      return !originalConfiguration.getDatabase().equals(databaseComboBox.getModel().getSelectedItem());
  }

  public void apply() {
    originalConfiguration.setDatabase((String) databaseComboBox.getModel().getSelectedItem());
    originalConfiguration.setModified();
  }

  public void reset() {
      databaseComboBox.getModel().setSelectedItem(originalConfiguration.getDatabase());
  }

  public void disposeUIResources() {
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.project.structure.facets.struts2.facet";
  }
}