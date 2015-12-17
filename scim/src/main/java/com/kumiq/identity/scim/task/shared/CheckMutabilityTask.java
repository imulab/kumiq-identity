package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ReplaceContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Enforce immutable rules
 *
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class CheckMutabilityTask<T extends Resource> implements Task<ReplaceContext<T>> {

    protected abstract ExceptionFactory.ResourceImmutabilityViolatedException violatedException(String path, String resourceId);

    @Override
    public void perform(ReplaceContext<T> context) {
        Assert.notNull(context.getResource());
        Assert.notNull(context.getOriginalCopy());
        Assert.notNull(context.getSchema());

        T resource = context.getResource();
        T original = context.getOriginalCopy();

        List<String> allPaths = context.getSchema().findAllPaths();
        for (String path : allPaths) {
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);

            // readWrite, writeOnly don't have to be checked, readOnly is not checked here
            if (!ScimConstants.MUTABILITY_IMMUTABLE.equals(attribute.getMutability()))
                continue;

            List<PathRef> compiledPaths = compilePath(path, context.getResource(), context.getSchema());
            compiledPaths.forEach(pathRef -> {
                EvaluationContext evaluationContextForResource = new EvaluationContext(resource);
                EvaluationContext evaluationContextForOriginal = new EvaluationContext(original);
                Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                        .withOption(Configuration.Option.COMPILE_WITH_HINT);
                Object resourceResult = pathRef.evaluate(evaluationContextForResource, evaluationConfiguration).getCursor();
                Object originalResult = pathRef.evaluate(evaluationContextForOriginal, evaluationConfiguration).getCursor();

                if (originalResult != null && !originalResult.equals(resourceResult))
                    throw violatedException(path, context.getId());
            });
        }
    }

    private List<PathRef> compilePath(String path, T resource, Schema schema) {
        CompilationContext compilationContext = CompilationContext
                .create(path, resource)
                .withSchema(schema);
        Configuration compilationConfiguration = Configuration
                .withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT)
                .withOption(Configuration.Option.SUPPRESS_EXCEPTION);
        return PathCompiler.compile(compilationContext, compilationConfiguration);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
