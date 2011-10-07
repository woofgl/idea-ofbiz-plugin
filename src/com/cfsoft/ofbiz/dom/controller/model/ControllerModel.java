package com.cfsoft.ofbiz.dom.controller.model;

import com.cfsoft.ofbiz.dom.controller.api.Controller;
import com.cfsoft.ofbiz.dom.controller.api.RequestMap;
import com.cfsoft.ofbiz.dom.controller.api.Response;
import com.cfsoft.ofbiz.dom.controller.api.ViewMap;
import com.intellij.psi.PsiClass;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.Processor;
import com.intellij.util.xml.model.DomModel;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.List;


/**
 * Services for accessing <code>controller.xml</code> files.
 *
 * @author Yann C&eacute;bron
 */
public interface ControllerModel extends DomModel<Controller> {

    /**
     * Get all {@link Controller} elements of the files belonging to this model.
     *
     * @return List.
     */
    @NotNull
    List<Controller> getMergedControllers();

    @NotNull
    List<RequestMap> getAllRequestMaps();

    @NotNull
    List<ViewMap> getAllViewMaps();

    List<Response> getAllResponses();


    @NotNull
    List<ViewMap> findViewMapByName(@NotNull @NonNls String viewName);


    boolean isServiceClass(PsiClass psiClass);

    boolean processViewMap(Processor<ViewMap> processor);

}

