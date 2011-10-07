package com.cfsoft.ofbiz;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

import java.awt.*;


public class IdeaUtils {
    public static Project getCurrentProject() {
        final Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        return PlatformDataKeys.PROJECT.getData(DataManager.getInstance().getDataContext(owner));
    }

    public static Editor getCurrentEditor() {
        final Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        return PlatformDataKeys.EDITOR.getData(DataManager.getInstance().getDataContext(owner));
    }

    public static Module getCurrentModule(){
        final Component owner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner();
        VirtualFile file = PlatformDataKeys.VIRTUAL_FILE.getData(DataManager.getInstance().getDataContext(owner));
        return ModuleUtil.findModuleForFile(file, getCurrentProject());
    }
}
