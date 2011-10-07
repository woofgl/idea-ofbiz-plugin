package com.cfsoft.ofbiz.dom.screen.model;

import com.cfsoft.ofbiz.dom.screen.api.Screen;
import com.cfsoft.ofbiz.dom.screen.api.Screens;
import com.cfsoft.ofbiz.dom.service.api.Service;
import com.cfsoft.ofbiz.dom.service.api.Services;
import com.intellij.util.xml.model.DomModel;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Services for accessing <code>controller.xml</code> files.
 *
 * @author Yann C&eacute;bron
 */
public interface ScreensModel extends DomModel<Screens> {
   @NotNull
    List<Screen> getAllScreens();


}

