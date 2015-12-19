package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ReplaceContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * After modification, if any multi-valued attributes were set to null, reset it to empty array.
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class PostModificationNullArrayProtectionTask<T extends Resource> implements Task<ReplaceContext<T>> {

    @Override
    public void perform(ReplaceContext<T> context) {
        Assert.notNull(context.getSchema());

        List<String> allPaths = context.getSchema().findAllPaths();
        allPaths.forEach(path -> {
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);
            if (!attribute.isMultiValued())
                return;

            compilePath(path, context.getResource(), context.getSchema()).forEach(pathRef -> {
                if (evaluateCompiledPath(pathRef, context.getResource()) == null) {
                    ModificationUnit modificationUnit = new ModificationUnit(ModificationUnit.Operation.REPLACE, path, new ArrayList<>());
                    ModificationContext modContext = new ModificationContext(
                            modificationUnit, context.getSchema(), context.getResource());
                    Configuration modConfig = Configuration.withResourceObjectProvider()
                            .withOption(Configuration.Option.COMPILE_WITH_HINT)
                            .withOption(Configuration.Option.SUPPRESS_EXCEPTION)
                            .withOption(Configuration.Option.TREAT_EMPTY_MULTIVALUE_AS_SIMPLE);
                    Modifier.create(pathRef, modContext, modConfig).modify();
                }
            });
        });
    }

    private Object evaluateCompiledPath(PathRef path, T resource) {
        EvaluationContext evaluationContext = new EvaluationContext(resource);
        Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT);
        return path.evaluate(evaluationContext, evaluationConfiguration).getCursor();
    }

    private List<PathRef> compilePath(String path, T resource, Schema schema) {
        CompilationContext compilationContext = CompilationContext
                .create(path, resource)
                .withSchema(schema);
        Configuration compilationConfiguration = Configuration
                .withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT)
                .withOption(Configuration.Option.SUPPRESS_EXCEPTION)
                .withOption(Configuration.Option.TREAT_EMPTY_MULTIVALUE_AS_SIMPLE);
        return PathCompiler.compile(compilationContext, compilationConfiguration);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
