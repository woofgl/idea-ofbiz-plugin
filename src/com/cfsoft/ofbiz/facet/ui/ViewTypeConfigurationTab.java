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

import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.cfsoft.ofbiz.facet.OfbizFacetConfiguration;
import com.intellij.facet.Facet;
import com.intellij.facet.frameworks.LibrariesDownloadAssistant;
import com.intellij.facet.frameworks.beans.Artifact;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.libraries.LibraryInfo;
import com.intellij.openapi.module.Module;
import com.intellij.ui.DocumentAdapter;
import com.intellij.util.Icons;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Struts2 facet tab "Features".
 *
 * @author Yann C&eacute;bron
 */
public class ViewTypeConfigurationTab extends FacetEditorTab {

    private JPanel myPanel;
    private JTextArea textArea;
    private boolean isModified = false;


    private final OfbizFacetConfiguration originalConfiguration;

    public ViewTypeConfigurationTab(final OfbizFacetConfiguration originalConfiguration,
                                    final FacetEditorContext editorContext) {
        this.originalConfiguration = originalConfiguration;
    }


    public void onFacetInitialized(@NotNull final Facet facet) {
        textArea.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                isModified = true;
            }
        });

    }

    @Nullable
    public Icon getIcon() {
        return Icons.TASK_ICON;
    }

    @Nls
    public String getDisplayName() {
        return "ViewMap Type Config";
    }

    public JComponent createComponent() {
        return myPanel;
    }

    public boolean isModified() {
        return isModified;
    }

    public void apply() {
        String[] lines = textArea.getText().split("\n");
        originalConfiguration.getViewTypes().clear();
        for (String line : lines) {
            OfbizFacetConfiguration.ViewType viewType = OfbizFacetConfiguration.ViewType.create(line);
            if (viewType != null) {
                originalConfiguration.getViewTypes().add(viewType);
            }
        }
        originalConfiguration.setModified();
    }

    public void reset() {
        StringBuilder buf = new StringBuilder();
        for (OfbizFacetConfiguration.ViewType viewType : originalConfiguration.getViewTypes()) {
            buf.append(viewType.getType()).append(":").
                    append(viewType.getExt()).append(":").append(viewType.hasTag()).append("\n");
        }
        textArea.setText(buf.toString());
        textArea.getDocument().addDocumentListener(new DocumentAdapter() {
            @Override
            protected void textChanged(DocumentEvent documentEvent) {
                isModified = true;
            }
        });
    }

    public void disposeUIResources() {
    }

    @Override
    public String getHelpTopic() {
        return "reference.settings.project.structure.facets.struts2.facet";
    }
}