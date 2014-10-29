package com.walien.plugins.generator;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiField;
import com.walien.plugins.builders.MethodNameBuilder;

public class FluentSetterGenerator extends AbstractMethodGenerator {

    public FluentSetterGenerator(Project project) {
        super(project);
    }

    @Override
    protected String buildCode(PsiField field) {
        PsiClass containingClass = field.getContainingClass();
        if (containingClass == null) {
            throw new IllegalStateException("No containing class found !");
        }
        String type = field.getType().getCanonicalText();
        return String.format("public %s %s(%s %s) { this.%s = %s; return this; }",
                containingClass.getName(),
                MethodNameBuilder.buildSetterName(field),
                type,
                field.getName(),
                field.getName(),
                field.getName()
        );
    }
}
