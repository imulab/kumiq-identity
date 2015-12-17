package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class IgnoreReadOnlyTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    private static final Logger log = LoggerFactory.getLogger(IgnoreReadOnlyTask.class);

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getResource());
        Assert.notNull(context.getSchema());

        List<String> allPaths = context.getSchema().findAllPaths();
        for (String path : allPaths) {
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);

            if (!ScimConstants.MUTABILITY_READONLY.equals(attribute.getMutability()))
                continue;

            PathRef compiledPath = compilePath(path, context.getResource(), context.getSchema());
            if (compiledPath == null)
                continue;

            EvaluationContext evaluationContext = new EvaluationContext(context.getResource());
            Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                    .withOption(Configuration.Option.COMPILE_WITH_HINT);
            Object result = compiledPath.evaluate(evaluationContext, evaluationConfiguration).getCursor();

            if (result != null) {
                ModificationUnit modificationUnit = new ModificationUnit(ModificationUnit.Operation.REMOVE, path, null);
                ModificationContext modificationContext = new ModificationContext(modificationUnit, context.getSchema(), context.getResource());
                Configuration modificationConfiguration = Configuration.withResourceObjectProvider()
                        .withOption(Configuration.Option.COMPILE_WITH_HINT);
                Modifier.create(compiledPath, modificationContext, modificationConfiguration).modify();
            }
        }
    }

    private PathRef compilePath(String path, T resource, Schema schema) {
        CompilationContext compilationContext = CompilationContext
                .create(path, resource)
                .withSchema(schema);
        Configuration compilationConfiguration = Configuration
                .withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT)
                .withOption(Configuration.Option.SUPPRESS_EXCEPTION);
        List<PathRef> compiledPaths = PathCompiler.compile(compilationContext, compilationConfiguration);
        if (compiledPaths.size() > 1) {
            log.warn("IgnoreReadOnly check only works on non-expandable paths");
            return null;
        } else {
            return compiledPaths.get(0);
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
