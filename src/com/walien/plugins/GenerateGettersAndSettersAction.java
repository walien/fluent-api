package com.walien.plugins;

import com.intellij.openapi.editor.actionSystem.EditorAction;

public class GenerateGettersAndSettersAction extends EditorAction {

    public GenerateGettersAndSettersAction() {
        super(new GenerateAllGettersAndSettersActionHandler());
    }
}
