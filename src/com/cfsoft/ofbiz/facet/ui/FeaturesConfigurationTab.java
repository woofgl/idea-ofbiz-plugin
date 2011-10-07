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
import com.intellij.facet.ui.libraries.FacetLibrariesValidator;
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
public class FeaturesConfigurationTab extends FacetEditorTab {

  private JPanel myPanel;
  private JComboBox versionComboBox;
  private JCheckBox disablePropertiesKeys;

  private final OfbizFacetConfiguration originalConfiguration;

  public FeaturesConfigurationTab(final OfbizFacetConfiguration originalConfiguration,
                                  final FacetEditorContext editorContext) {
    this.originalConfiguration = originalConfiguration;

    disablePropertiesKeys.setSelected(originalConfiguration.isPropertiesKeysDisabled());

    final Module module = editorContext.getModule();
    //final String version = OfbizVersionDetector.detectStrutsVersion(module);
    final String version = "11.04";
    if (version != null) {
      versionComboBox.setModel(new DefaultComboBoxModel(new String[]{version}));
      versionComboBox.getModel().setSelectedItem(version);
      versionComboBox.setEnabled(false);
      return;
    }

  }


  @Nullable
  private Artifact getSelectedVersion() {
    final Object version = versionComboBox.getModel().getSelectedItem();
    return version instanceof Artifact ? (Artifact) version : null;
  }

  @Nullable
  private LibraryInfo[] getRequiredLibraries() {
    final Artifact version = getSelectedVersion();

    return version == null ? null : LibrariesDownloadAssistant.getLibraryInfos(version);
  }

  public void onFacetInitialized(@NotNull final Facet facet) {

  }

  @Nullable
  public Icon getIcon() {
    return Icons.TASK_ICON;
  }

  @Nls
  public String getDisplayName() {
    return "not usage";
  }

  public JComponent createComponent() {
    return myPanel;
  }

  public boolean isModified() {
    return originalConfiguration.isPropertiesKeysDisabled() !=
           disablePropertiesKeys.isSelected();
  }

  public void apply() {
    originalConfiguration.setPropertiesKeysDisabled(disablePropertiesKeys.isSelected());
    originalConfiguration.setModified();
  }

  public void reset() {
  }

  public void disposeUIResources() {
  }

  @Override
  public String getHelpTopic() {
    return "reference.settings.project.structure.facets.struts2.facet";
  }
}