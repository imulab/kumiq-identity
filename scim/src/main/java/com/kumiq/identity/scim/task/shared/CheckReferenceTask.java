package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.task.ResourceOpContext;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public abstract class CheckReferenceTask<T extends Resource> implements Task<ResourceOpContext<T>> {

    private ResourceDatabase.GroupDatabase groupDatabase;
    private ResourceDatabase.UserDatabase userDatabase;

    protected abstract ExceptionFactory.ResourceReferenceViolatedException violationException(String path, String resourceId);

    @Override
    public void perform(ResourceOpContext<T> context) {
        Assert.notNull(context.getSchema());

        List<String> allPaths = context.getSchema().findAllPaths();
        for (String path : allPaths) {
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);
            if (CollectionUtils.isEmpty(attribute.getReferenceTypes()))
                continue;

            boolean skipUserCheck = !attribute.getReferenceTypes().contains(ScimConstants.REF_TYPE_USER);
            boolean skipGroupCheck = !attribute.getReferenceTypes().contains(ScimConstants.REF_TYPE_GROUP);
            if (skipUserCheck && skipGroupCheck)
                continue;

            boolean userMatch = true, groupMatch = true;
            List<PathRef> compiledPaths = compilePath(path, context.getResource(), context.getSchema());
            for (PathRef pathRef : compiledPaths) {
                EvaluationContext evaluationContext = new EvaluationContext(context.getResource());
                Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                        .withOption(Configuration.Option.COMPILE_WITH_HINT);
                Object id = pathRef.evaluate(evaluationContext, evaluationConfiguration).getCursor();

                if (!skipUserCheck) {
                    if (!userDatabase.findById(id.toString()).isPresent()) {
                        userMatch = false;
                        skipUserCheck = true;
                    }
                }

                if (!skipGroupCheck) {
                    if (!groupDatabase.findById(id.toString()).isPresent()) {
                        groupMatch = false;
                        skipGroupCheck = true;
                    }
                }

                if (skipUserCheck && skipGroupCheck)
                    break;
            }

            if (!userMatch || !groupMatch)
                throw violationException(path, context.getResource().getId());
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
        Assert.notNull(userDatabase);
        Assert.notNull(groupDatabase);
    }

    public ResourceDatabase.GroupDatabase getGroupDatabase() {
        return groupDatabase;
    }

    public void setGroupDatabase(ResourceDatabase.GroupDatabase groupDatabase) {
        this.groupDatabase = groupDatabase;
    }

    public ResourceDatabase.UserDatabase getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase userDatabase) {
        this.userDatabase = userDatabase;
    }
}
