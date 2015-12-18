package com.kumiq.identity.scim.task.shared;

import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.task.PatchContext;
import com.kumiq.identity.scim.task.Task;
import org.springframework.util.Assert;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ApplyModificationUnitsTask<T extends Resource> implements Task<PatchContext<T>> {

    @Override
    public void perform(PatchContext<T> context) {
        Assert.notNull(context.getSchema());
        Assert.notNull(context.getResource());

        context.getModifications().forEach(modificationUnit -> {
            ModificationContext modContext = new ModificationContext(
                    modificationUnit, context.getSchema(), context.getResource());
            Configuration modConfig = Configuration.withResourceObjectProvider()
                    .withOption(Configuration.Option.COMPILE_WITH_HINT)
                    .withOption(Configuration.Option.SUPPRESS_EXCEPTION);

            CompilationContext compileContext = CompilationContext
                    .create(modificationUnit.getPath(), context.getResource())
                    .withSchema(context.getSchema());
            Configuration compileConfig = Configuration.withResourceObjectProvider()
                    .withOption(Configuration.Option.COMPILE_WITH_HINT)
                    .withOption(Configuration.Option.SUPPRESS_EXCEPTION);

            PathCompiler
                    .compile(compileContext, compileConfig)
                    .forEach(pathRef -> Modifier.create(pathRef, modContext, modConfig).modify());
        });
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
