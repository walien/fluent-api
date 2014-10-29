package com.walien.plugins;

import com.google.common.base.Optional;
import com.intellij.codeInsight.hint.HintManager;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.actionSystem.EditorActionHandler;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiField;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.walien.plugins.builders.MethodNameBuilder;
import com.walien.plugins.domain.FieldMethodGeneration;
import com.walien.plugins.generator.AbstractMethodGenerator;
import com.walien.plugins.generator.FluentSetterGenerator;
import com.walien.plugins.generator.GetterGenerator;

import java.util.ArrayList;
import java.util.List;

public class GenerateAllGettersAndSettersActionHandler extends EditorActionHandler {

    @Override
    public void execute(Editor editor, DataContext dataContext) {

        Optional<PsiClass> concernedClass = getConcernedClass(editor, dataContext);
        if (!concernedClass.isPresent()) {
            return;
        }

        Project project = DataKeys.PROJECT.getData(dataContext);

        List<FieldMethodGeneration> concernedFields = getConcernedFields(concernedClass.get());
        if (concernedFields.isEmpty()) {
            HintManager.getInstance().showErrorHint(editor, "No fields found.");
            return;
        }

        generateMethods(project, concernedClass.get(), concernedFields);
    }

    private Optional<PsiClass> getConcernedClass(Editor editor, DataContext dataContext) {

        PsiFile file = DataKeys.PSI_FILE.getData(dataContext);
        if (file == null) {
            return Optional.absent();
        }

        int offset = editor.getCaretModel().getOffset();
        PsiElement element = file.findElementAt(offset);

        if (element == null) {
            return Optional.absent();
        }

        PsiClass clazz = PsiTreeUtil.getParentOfType(element, PsiClass.class);
        if (clazz == null) {
            HintManager.getInstance().showErrorHint(editor, "No containing class found.");
            return Optional.absent();
        }
        if (clazz.isInterface()) {
            HintManager.getInstance().showErrorHint(editor, "Unable to create getters/setters on interface.");
            return Optional.absent();
        }

        return Optional.of(clazz);
    }

    private List<FieldMethodGeneration> getConcernedFields(PsiClass concernedClass) {

        List<FieldMethodGeneration> concernedFields = new ArrayList<FieldMethodGeneration>();

        for (PsiField psiField : concernedClass.getFields()) {

            FieldMethodGeneration methodGeneration = new FieldMethodGeneration().setField(psiField);
            methodGeneration.setGenerateGetter(
                    concernedClass.findMethodsByName(MethodNameBuilder.buildGetterName(psiField), false).length == 0
            );
            methodGeneration.setGenerateSetter(
                    concernedClass.findMethodsByName(MethodNameBuilder.buildSetterName(psiField), false).length == 0
            );

            concernedFields.add(methodGeneration);
        }

        return concernedFields;
    }

    private void generateMethods(Project project, PsiClass clazz, List<FieldMethodGeneration> fields) {

        AbstractMethodGenerator getterGenerator = new GetterGenerator(project);
        AbstractMethodGenerator setterGenerator = new FluentSetterGenerator(project);

        for (FieldMethodGeneration generation : fields) {

            if (generation.isGenerateGetter()) {
                getterGenerator.generate(clazz, generation.getField());
            }

            if (generation.isGenerateSetter()) {
                setterGenerator.generate(clazz, generation.getField());
            }
        }
    }
}
