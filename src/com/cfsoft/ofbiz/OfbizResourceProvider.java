package com.cfsoft.ofbiz;

import com.intellij.javaee.ResourceRegistrar;
import com.intellij.javaee.StandardResourceProvider;
import org.jetbrains.annotations.NonNls;


public class OfbizResourceProvider implements StandardResourceProvider {
    @NonNls
    private static final String DTD_PATH = "/resources/dtds";
    private static final String[] XSDS = {"component-loader.xsd",
            "datafiles.xsd", "entity-config.xsd", "entity-eca.xsd", "entitygroup.xsd",
            "entitymodel.xsd", "fieldtypemodel.xsd", "jndi-config.xsd",
            "ofbiz-component.xsd", "ofbiz-containers.xsd", "ofbiz-properties.xsd",
            "regions.xsd", "security-config.xsd", "SeleniumXml.xsd", "service-config.xsd",
            "service-eca.xsd", "service-group.xsd", "service-mca.xsd",
            "services.xsd", "simple-methods.xsd","simple-methods-v2.xsd", "site-conf.xsd", "test-suite.xsd",
            "widget-form.xsd", "widget-menu.xsd", "widget-screen.xsd", "widget-tree.xsd"};

    @Override
    public void registerResources(ResourceRegistrar resourceRegistrar) {
        addDTDResource("http://www.ofbiz.org/dtds","4", resourceRegistrar);
        addDTDResource("http://ofbiz.apache.org/dtds","12", resourceRegistrar);
    }

    /**
     * Adds a DTD resource from local DTD resource path.
     *
     * @param uri       Resource URI.
     * @param localFile DTD filename.
     * @param registrar Resource registrar.
     */
    private static void addDTDResource(@NonNls final String uri,
                                       @NonNls final String localFile,
                                       final ResourceRegistrar registrar) {
        for (String xsd : XSDS) {
           registrar.addStdResource(String.format("%s/%s", uri, xsd),
                   String.format("%s/%s/%s", DTD_PATH, localFile,xsd),OfbizResourceProvider.class);
        }

    }
}
