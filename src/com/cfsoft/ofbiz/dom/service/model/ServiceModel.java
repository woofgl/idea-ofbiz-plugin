package com.cfsoft.ofbiz.dom.service.model;

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
public interface ServiceModel extends DomModel<Services> {
   @NotNull
    List<Service> getAllServices();


}

