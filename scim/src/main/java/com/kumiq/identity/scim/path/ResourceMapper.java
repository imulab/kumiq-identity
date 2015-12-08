package com.kumiq.identity.scim.path;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
public class ResourceMapper {

    /**
     * Converts the given object to Map, with the help of schema and path inclusion / exclusion hints.
     *
     * @param context
     * @param configuration
     * @return
     */
    Map<String, Object> convertToMap(MappingContext context, Configuration configuration) {
        Map<String, Object> resultMap = new HashMap<>();

        for (String pathToInclude : context.getIncludePaths()) {
            CompilationContext compilationContext = new CompilationContext(pathToInclude, context.getData());
            PathCompiler.compile(compilationContext, configuration).stream().forEach(pathRef -> {
                EvaluationContext evaluationContext = new EvaluationContext(context.getData());
                evaluationContext = pathRef.evaluate(evaluationContext, configuration);
                Object evaluationResult = evaluationContext.getCursor();

                // TODO
                // 1. Schema should be in compilation process
                // 2. also expand simple path but multiValued node

                ModificationUnit modificationUnit = new ModificationUnit(ModificationUnit.Operation.ADD, pathToInclude, evaluationResult);
                ModificationContext modificationContext = new ModificationContext(modificationUnit, context.getSchema(), resultMap);
                Modifier.create(pathRef, modificationContext, Configuration.withMapObjectProvider()).modify();
            });
        }

        for (String pathToExclude : context.getExcludePaths()) {
            CompilationContext compilationContext = new CompilationContext(pathToExclude, context.getData());
            PathCompiler.compile(compilationContext, configuration).stream().forEach(pathRef -> {
                ModificationUnit modificationUnit = new ModificationUnit(ModificationUnit.Operation.REMOVE, pathToExclude, null);
                ModificationContext modificationContext = new ModificationContext(modificationUnit, context.getSchema(), resultMap);
                Modifier.create(pathRef, modificationContext, Configuration.withMapObjectProvider()).modify();
            });
        }

        return resultMap;
    }
}
