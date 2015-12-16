package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.constant.ScimConstants;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import com.kumiq.identity.scim.utils.TypeUtils;
import com.kumiq.identity.scim.utils.ValueUtils;
import org.springframework.util.Assert;

import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckUniquenessTask<T extends User> implements Task<UserCreateContext<T>> {

    private ResourceDatabase.UserDatabase<T> userDatabase;

    /**
     * Useful in update context, where the only duplicate value exists in database should be the
     * resource being updated.
     */
    private boolean allowSelf = false;

    @Override
    public void perform(UserCreateContext<T> context) {
        Assert.notNull(context.getSchema());

        List<String> allPaths = context.getSchema().findAllPaths();
        for (String path : allPaths) {
            Schema.Attribute attribute = context.getSchema().findAttributeByPath(path);
            if (ScimConstants.UNIQUENESS_SERVER.equals(attribute.getUniqueness())) {
                Object evalResult = evaluatePath(path, context.getResource(), context.getSchema());
                String query = constructQuery(path, evalResult);
                List<T> queryResults = userDatabase.query(query, "meta.created", true);

                if (allowSelf) {
                    if ((queryResults.size() > 1) ||
                            (queryResults.size() == 1 && !queryResults.get(0).getId().equals(context.getResource().getId())))
                        throw ExceptionFactory.userPathNotUnique(path, context.getResource().getId());
                } else {
                    if (queryResults.size() > 0)
                        throw ExceptionFactory.userPathNotUnique(path, context.getResource().getId());
                }
            }
        }
    }

    private String constructQuery(String path, Object evalResult) {
        String query = path + " eq ";
        if (TypeUtils.isString(evalResult))
            query += ValueUtils.asScimString(TypeUtils.asString(evalResult));
        else if (TypeUtils.isDate(evalResult))
            query += ValueUtils.asScimDate(TypeUtils.asDate(evalResult));
        else
            query += evalResult.toString();
        return query;
    }

    private Object evaluatePath(String path, T resource, Schema schema) {
        CompilationContext compilationContext = CompilationContext
                .create(path, resource)
                .withSchema(schema);
        Configuration compilationConfiguration = Configuration
                .withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT)
                .withOption(Configuration.Option.SUPPRESS_EXCEPTION);
        List<PathRef> pathRefs = PathCompiler.compile(compilationContext, compilationConfiguration);
        if (pathRefs.size() > 1)
            throw ExceptionFactory.userPathNotUnique(path, resource.getId());

        EvaluationContext evaluationContext = new EvaluationContext(resource);
        Configuration evaluationConfiguration = Configuration.withResourceObjectProvider()
                .withOption(Configuration.Option.COMPILE_WITH_HINT);
        return pathRefs.get(0).evaluate(evaluationContext, evaluationConfiguration).getCursor();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(userDatabase);
    }

    public boolean isAllowSelf() {
        return allowSelf;
    }

    public void setAllowSelf(boolean allowSelf) {
        this.allowSelf = allowSelf;
    }

    public ResourceDatabase.UserDatabase<T> getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase<T> userDatabase) {
        this.userDatabase = userDatabase;
    }
}
