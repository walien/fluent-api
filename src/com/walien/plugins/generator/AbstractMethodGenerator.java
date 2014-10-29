package com.walien.plugins.generator;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleManager;

public abstract class AbstractMethodGenerator {

    private final CodeStyleManager codeStyleManager;
    private final PsiElementFactory factory;

    protected AbstractMethodGenerator(Project project) {
        factory = JavaPsiFacade.getElementFactory(project);
        codeStyleManager = CodeStyleManager.getInstance(project);
    }

    protected abstract String buildCode(PsiField field);

    public final void generate(final PsiClass clazz, PsiField field) {

        String code = buildCode(field);

        final PsiMethod method = factory.createMethodFromText(code, null);
        codeStyleManager.reformat(method);

        ApplicationManager.getApplication().runWriteAction(new Runnable() {
            @Override
            public void run() {
                clazz.add(method);
            }
        });
    }
}
