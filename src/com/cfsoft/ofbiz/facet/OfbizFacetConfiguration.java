package com.cfsoft.ofbiz.facet;

import com.cfsoft.ofbiz.facet.ui.*;
import com.intellij.facet.FacetConfiguration;
import com.intellij.facet.ui.FacetEditorContext;
import com.intellij.facet.ui.FacetEditorTab;
import com.intellij.facet.ui.FacetValidatorsManager;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.ModificationTracker;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.vfs.pointers.VirtualFilePointer;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;


public class OfbizFacetConfiguration implements FacetConfiguration, ModificationTracker, Disposable {

    // Filesets
    @NonNls
    private static final String CONTROLLER_FILESET = "cfileset";
    private static final String SERVICE_FILESET = "sfileset";
    private static final String COMPONENT_FILESET = "componentfileset";
    private static final String DATABASE_KEY = "database";
    private static final String VIEW_TYPE_MAP = "viewtypemap";
    private static final String DATABASE_CONFIG = "db";
    @NonNls
    private static final String SET_ID = "id";
    @NonNls
    private static final String SET_NAME = "name";
    @NonNls
    private static final String SET_REMOVED = "removed";
    @NonNls
    private static final String FILE = "file";


    private final Set<OfbizFileSet> myControllerFileSets = new LinkedHashSet<OfbizFileSet>();
    private final Set<OfbizFileSet> myServiceFileSets = new LinkedHashSet<OfbizFileSet>();
    private final Set<OfbizFileSet> myComponentFileSets = new LinkedHashSet<OfbizFileSet>();

    private long myModificationCount;

    // Features - tab
    private static final String PROPERTIES_KEYS = "propertiesKeys";

    private static final String PROPERTIES_KEYS_DISABLED = "disabled";

    private boolean myPropertiesKeysDisabled = false;
    private static final String SFILE = "sfile";
    private String database = "mysql";

    private Set<ViewType> viewTypes = new HashSet<ViewType>();


    /**
     * Gets the currently configured filesets.
     *
     * @return Filesets.
     */
    public Set<OfbizFileSet> getControllerFileSets() {
        return myControllerFileSets;
    }

    /**
     * Gets the currently configured filesets.
     *
     * @return Filesets.
     */
    Set<OfbizFileSet> getServiceFileSets() {
        return myServiceFileSets;
    }

    Set<OfbizFileSet> getComponentFileSets() {
        return myComponentFileSets;
    }

    public boolean isPropertiesKeysDisabled() {
        return myPropertiesKeysDisabled;
    }

    public void setPropertiesKeysDisabled(final boolean myPropertiesKeysDisabled) {
        this.myPropertiesKeysDisabled = myPropertiesKeysDisabled;
    }

    public FacetEditorTab[] createEditorTabs(FacetEditorContext editorContext, FacetValidatorsManager facetValidatorsManager) {
        return new FacetEditorTab[]{
//                new ControllerFileSetConfigurationTab(this, editorContext),
//                new ServiceFileSetConfigurationTab(this, editorContext),
//                new ComponentFileSetConfigurationTab(this, editorContext),
                new DatabaseConfigurationTab(this, editorContext),
//                new ViewTypeConfigurationTab(this, editorContext)
        };
//        return new FacetEditorTab[]{};
    }


    public void readExternal(final Element element) throws InvalidDataException {
        for (final Object setElement : element.getChildren(CONTROLLER_FILESET)) {
            final String setName = ((Element) setElement).getAttributeValue(SET_NAME);
            final String setId = ((Element) setElement).getAttributeValue(SET_ID);
            final String removed = ((Element) setElement).getAttributeValue(SET_REMOVED);
            if (setName != null && setId != null) {
                final OfbizFileSet fileSet = new OfbizFileSet(setId, setName, this);
                final List files = ((Element) setElement).getChildren(FILE);
                for (final Object fileElement : files) {
                    final String text = ((Element) fileElement).getText();
                    fileSet.addFile(text);
                }
                fileSet.setRemoved(Boolean.valueOf(removed));
                myControllerFileSets.add(fileSet);
            }
        }
        for (final Object setElement : element.getChildren(SERVICE_FILESET)) {
            final String setName = ((Element) setElement).getAttributeValue(SET_NAME);
            final String setId = ((Element) setElement).getAttributeValue(SET_ID);
            final String removed = ((Element) setElement).getAttributeValue(SET_REMOVED);
            if (setName != null && setId != null) {
                final OfbizFileSet fileSet = new OfbizFileSet(setId, setName, this);
                final List files = ((Element) setElement).getChildren(SFILE);
                for (final Object fileElement : files) {
                    final String text = ((Element) fileElement).getText();
                    fileSet.addFile(text);
                }
                fileSet.setRemoved(Boolean.valueOf(removed));
                myServiceFileSets.add(fileSet);
            }
        }
        for (final Object setElement : element.getChildren(COMPONENT_FILESET)) {
            final String setName = ((Element) setElement).getAttributeValue(SET_NAME);
            final String setId = ((Element) setElement).getAttributeValue(SET_ID);
            final String removed = ((Element) setElement).getAttributeValue(SET_REMOVED);
            if (setName != null && setId != null) {
                final OfbizFileSet fileSet = new OfbizFileSet(setId, setName, this);
                final List files = ((Element) setElement).getChildren(SFILE);
                for (final Object fileElement : files) {
                    final String text = ((Element) fileElement).getText();
                    fileSet.addFile(text);
                }
                fileSet.setRemoved(Boolean.valueOf(removed));
                myComponentFileSets.add(fileSet);
            }
        }

        for (final Object setElement : element.getChildren(VIEW_TYPE_MAP)) {
            final String type = ((Element) setElement).getAttributeValue("type");
            final String ext = ((Element) setElement).getAttributeValue("ext");
            final String tag = ((Element) setElement).getAttributeValue("tag");
            if (type != null && ext != null) {
                viewTypes.add(new ViewType(type, ext, Boolean.valueOf(tag)));
            }
        }
        viewTypes.add(new ViewType("screen", "xml", true));
        viewTypes.add(new ViewType("form", "xml", true));
        viewTypes.add(new ViewType("birt", "rptdesign", false));

        // new in X
        final Element propertiesElement = element.getChild(PROPERTIES_KEYS);
        if (propertiesElement != null) {
            final String disabled = propertiesElement.getAttributeValue(PROPERTIES_KEYS_DISABLED);
            myPropertiesKeysDisabled = Boolean.valueOf(disabled);
        }
        //read database config
        final Element databaseElement = element.getChild(DATABASE_KEY);
        if (databaseElement != null) {
            database = databaseElement.getAttributeValue(DATABASE_CONFIG);
        } else {
            database = "mysql";
        }

    }

    public void writeExternal(final Element element) throws WriteExternalException {
        for (final OfbizFileSet fileSet : myControllerFileSets) {
            final Element setElement = new Element(CONTROLLER_FILESET);
            setElement.setAttribute(SET_ID, fileSet.getId());
            setElement.setAttribute(SET_NAME, fileSet.getName());
            setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
            element.addContent(setElement);

            for (final VirtualFilePointer fileName : fileSet.getFiles()) {
                final Element fileElement = new Element(FILE);
                fileElement.setText(fileName.getUrl());
                setElement.addContent(fileElement);
            }
        }
        for (final OfbizFileSet fileSet : myServiceFileSets) {
            final Element setElement = new Element(SERVICE_FILESET);
            setElement.setAttribute(SET_ID, fileSet.getId());
            setElement.setAttribute(SET_NAME, fileSet.getName());
            setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
            element.addContent(setElement);

            for (final VirtualFilePointer fileName : fileSet.getFiles()) {
                final Element fileElement = new Element(SFILE);
                fileElement.setText(fileName.getUrl());
                setElement.addContent(fileElement);
            }
        }
        for (final OfbizFileSet fileSet : myComponentFileSets) {
            final Element setElement = new Element(COMPONENT_FILESET);
            setElement.setAttribute(SET_ID, fileSet.getId());
            setElement.setAttribute(SET_NAME, fileSet.getName());
            setElement.setAttribute(SET_REMOVED, Boolean.toString(fileSet.isRemoved()));
            element.addContent(setElement);

            for (final VirtualFilePointer fileName : fileSet.getFiles()) {
                final Element fileElement = new Element(SFILE);
                fileElement.setText(fileName.getUrl());
                setElement.addContent(fileElement);
            }
        }
        for (final ViewType viewtype : viewTypes) {
            final Element setElement = new Element(VIEW_TYPE_MAP);
            setElement.setAttribute("type", viewtype.getType());
            setElement.setAttribute("ext", viewtype.getExt());
            setElement.setAttribute("tag", Boolean.toString(viewtype.hasTag()));
            element.addContent(setElement);
        }
//
        final Element databaseElement = new Element(DATABASE_KEY);
        databaseElement.setAttribute(DATABASE_CONFIG, getDatabase());
        element.addContent(databaseElement);
    }

    public long getModificationCount() {
        return myModificationCount;
    }

    public void setModified() {
        myModificationCount++;
    }

    public void dispose() {
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public Set<ViewType> getViewTypes() {
        return viewTypes;
    }

    public static class ViewType {
        private final static Pattern regex = Pattern.compile("^\\s*(\\w+)\\s*:\\s*(\\w+)\\s*(:\\s*(\\w+)\\s*)?$");

        String type;
        String ext;
        boolean hasTag;

        public ViewType(String type, String ext, boolean hasTag) {
            this.type = type;
            this.ext = ext;
            this.hasTag = hasTag;
        }

        public ViewType(String type, String ext) {
            this.type = type;
            this.ext = ext;
            this.hasTag = false;
        }

        public String getType() {
            return type;
        }

        public String getExt() {
            return ext;
        }

        public boolean hasTag() {
            return hasTag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            ViewType viewType = (ViewType) o;
            if (type != null ? !type.equals(viewType.type) : viewType.type != null) return false;
            return true;
        }

        @Override
        public int hashCode() {
            int result = type != null ? type.hashCode() : 0;
            return result;
        }

        public static ViewType create(String line) {
             try {

                Matcher matcher = regex.matcher(line);
                if (matcher.find()) {
                    String tag = matcher.group(4);
                    ViewType viewType = new ViewType(matcher.group(1),matcher.group(2),Boolean.valueOf(tag));
                    return viewType;
                }
            } catch (PatternSyntaxException ex) {
                // Syntax error in the regular expression
            }
            return null;
        }
    }
}
