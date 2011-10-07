/*
 * Copyright 2011 The authors
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

package com.cfsoft.ofbiz.facet;

import com.cfsoft.ofbiz.Constants;
import com.cfsoft.ofbiz.facet.ui.OfbizConfigsSercher;
import com.cfsoft.ofbiz.facet.ui.OfbizControllerConfigsSearcher;
import com.cfsoft.ofbiz.facet.ui.OfbizFileSet;
import com.cfsoft.ofbiz.facet.ui.OfbizServiceConfigsSearcher;
import com.intellij.facet.ui.FacetBasedFrameworkSupportProvider;
import com.intellij.ide.util.frameworkSupport.FrameworkVersion;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationListener;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.roots.ui.configuration.ModulesConfigurator;
import com.intellij.openapi.startup.StartupManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.util.containers.MultiMap;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.event.HyperlinkEvent;
import java.util.*;

/**
 * "Add Framework" support.
 *
 * @author Yann C&eacute;bron
 */
public class OfbizFrameworkSupportProvider extends FacetBasedFrameworkSupportProvider<OfbizFacet> {

    private static final Logger LOG = Logger.getInstance(OfbizFrameworkSupportProvider.class.getName());

    protected OfbizFrameworkSupportProvider() {
        super(OfbizFacetType.getInstance());
    }

    public String getTitle() {
        return UIUtil.replaceMnemonicAmpersand("Ofbiz");
    }

    @NotNull
    public List<FrameworkVersion> getVersions() {
        final List<FrameworkVersion> result = new ArrayList<FrameworkVersion>();
        return result;
    }

    protected void setupConfiguration(final OfbizFacet strutsFacet,
                                      final ModifiableRootModel modifiableRootModel, final FrameworkVersion version) {
    }

    @Override
    protected void onFacetCreated(final OfbizFacet ofbizFacet,
                                  final ModifiableRootModel modifiableRootModel,
                                  final FrameworkVersion version) {
        final Module module = ofbizFacet.getModule();
        StartupManager.getInstance(module.getProject()).runWhenProjectIsInitialized(new Runnable() {
            public void run() {
                final VirtualFile[] sourceRoots = ModuleRootManager.getInstance(module).getSourceRoots();
                if (sourceRoots.length <= 0) {
                    return;
                }

                final PsiDirectory directory = PsiManager.getInstance(module.getProject()).findDirectory(sourceRoots[0]);
                if (directory == null ||
                        directory.findFile(Constants.CONTROLLER_XML_DEFAULT_FILENAME) != null) {
                    return;
                }


                try {
                    final OfbizFacetConfiguration ofbizFacetConfiguration = ofbizFacet.getConfiguration();

                    final Set<OfbizFileSet> empty = Collections.emptySet();
                    final OfbizFileSet controllerFileSet = new OfbizFileSet(OfbizFileSet.getUniqueId(empty),
                            OfbizFileSet.getUniqueName("Controller Default File Set", empty),
                            ofbizFacetConfiguration);

                    final OfbizConfigsSercher searcher = new OfbizControllerConfigsSearcher(module);
                    searcher.search();
                    final MultiMap<Module, PsiFile> configFiles = searcher.getFilesByModules();
                    for (PsiFile psiFile : configFiles.values()) {
                        controllerFileSet.addFile(psiFile.getVirtualFile());
                    }
                    ofbizFacetConfiguration.getControllerFileSets().add(controllerFileSet);
                    //service fileset
                    final OfbizFileSet serviceFileSet = new OfbizFileSet(OfbizFileSet.getUniqueId(empty),
                            OfbizFileSet.getUniqueName("Services Default File Set", empty),
                            ofbizFacetConfiguration);
                    final OfbizConfigsSercher serviceSearcher = new OfbizServiceConfigsSearcher(module);
                    serviceSearcher.search();
                    final MultiMap<Module, PsiFile> serviceConfigFiles = serviceSearcher.getFilesByModules();
                    for (PsiFile psiFile : serviceConfigFiles.values()) {
                        serviceFileSet.addFile(psiFile.getVirtualFile());
                    }
                    ofbizFacetConfiguration.getServiceFileSets().add(serviceFileSet);


                    final NotificationListener showFacetSettingsListener = new NotificationListener() {
                        public void hyperlinkUpdate(@NotNull final Notification notification,
                                                    @NotNull final HyperlinkEvent event) {
                            if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                notification.expire();
                                ModulesConfigurator.showFacetSettingsDialog(ofbizFacet, null);
                            }
                        }
                    };

                    Notifications.Bus.notify(
                            new Notification("Ofbiz", "Ofbiz Setup",
                                    "Ofbiz Facet has been created, please check <a href=\"more\">created fileset</a>",
                                    NotificationType.INFORMATION,
                                    showFacetSettingsListener),
                            module.getProject());

                } catch (Exception e) {
                    LOG.error("error creating struts.xml from template", e);
                }
            }
        });
    }

}
