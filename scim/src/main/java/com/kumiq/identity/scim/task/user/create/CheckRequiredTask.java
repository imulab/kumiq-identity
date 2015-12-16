package com.kumiq.identity.scim.task.user.create;

import com.kumiq.identity.scim.database.ResourceDatabase;
import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.task.Task;
import com.kumiq.identity.scim.task.UserCreateContext;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class CheckRequiredTask<T extends User> implements Task<UserCreateContext<T>> {

    private ResourceDatabase.UserDatabase<T> userDatabase;

    /**
     * Paths which if have violated the "required" constraint, will be ignored
     */
    private List<String> amnestyPaths = new ArrayList<>();

    @Override
    public void perform(UserCreateContext<T> context) {
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
                    throw ExceptionFactory.userAttributeAbsent(path, context.getResource().getId());
                else if (attribute.isMultiValued()) {
                    Assert.isTrue(TypeUtils.isCollection(result));
                    if (CollectionUtils.isEmpty(TypeUtils.asCollection(result)))
                        throw ExceptionFactory.userAttributeAbsent(path, context.getResource().getId());
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
        Assert.notNull(userDatabase);
    }

    public ResourceDatabase.UserDatabase<T> getUserDatabase() {
        return userDatabase;
    }

    public void setUserDatabase(ResourceDatabase.UserDatabase<T> userDatabase) {
        this.userDatabase = userDatabase;
    }

    public List<String> getAmnestyPaths() {
        return amnestyPaths;
    }

    public void setAmnestyPaths(List<String> amnestyPaths) {
        this.amnestyPaths = amnestyPaths;
    }
}
