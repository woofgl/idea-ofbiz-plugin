package com.cfsoft.ofbiz.facet;


import com.cfsoft.ofbiz.Constants;
import com.cfsoft.ofbiz.dom.component.api.Component;
import com.intellij.facet.FacetType;
import com.intellij.framework.detection.FacetBasedFrameworkDetector;
import com.intellij.framework.detection.FileContentPattern;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.patterns.ElementPattern;
import com.intellij.util.indexing.FileContent;
import org.jetbrains.annotations.NotNull;

public class OfbizFrameworkDetector extends FacetBasedFrameworkDetector<OfbizFacet, OfbizFacetConfiguration> {


    public OfbizFrameworkDetector() {
        super("ofbiz");
    }

    @Override
    public FacetType<OfbizFacet, OfbizFacetConfiguration> getFacetType() {
        return OfbizFacetType.getInstance();
    }

    @NotNull
    @Override
    public FileType getFileType() {
        return StdFileTypes.XML;
    }

    @NotNull
    @Override
    public ElementPattern<FileContent> createSuitableFilePattern() {
        return FileContentPattern.fileContent()
        .withName(Constants.COMPONENT_XML_DEFAULT_FILENAME)
                .xmlWithRootTag(Component.TAG_NAME);
    }
}
