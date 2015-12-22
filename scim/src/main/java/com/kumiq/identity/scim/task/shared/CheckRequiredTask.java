package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.exception.ExceptionFactory;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class CheckRequiredTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    /**
     * Paths which if have violated the "required" constraint, will be ignored
     */
    private List<String> amnestyPaths = new ArrayList<>();

    protected abstract ExceptionFactory.ResourceAttributeAbsentException violationException(String path, String resourceId);

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getSchema());

        List<String> allPaths = context.getSchema().findAllPaths();
        for (String path : allPaths) {
            if (amnestyPaths.contains(path))
                continue;

            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);
            if (!attribute.isRequired())
                continue;

            List<PathRef> compiledPaths = compilePath(path, context.getResource(), context.getSchema());
            compiledPaths.forEach(pathRef -> {
                EvaluationContext evaluationContext = new EvaluationContext(context.getResource());
                Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                        .withOption(Configuration.Option.COMPILE_WITH_HINT);
                Object result = pathRef.evaluate(evaluationContext, evaluationConfiguration).getCursor();

                if (result == null)
                    throw violationException(path, context.getResource().getId());

                if (attribute.isMultiValued() && TypeUtils.isCollection(result)) {
                    if (CollectionUtils.isEmpty(TypeUtils.asCollection(result)))
                        throw violationException(path, context.getResource().getId());
                }
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

    public List<String> getAmnestyPaths() {
        return amnestyPaths;
    }

    public void setAmnestyPaths(List<String> amnestyPaths) {
        this.amnestyPaths = amnestyPaths;
    }
}
