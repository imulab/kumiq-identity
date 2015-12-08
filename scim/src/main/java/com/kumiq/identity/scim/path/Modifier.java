package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
@SuppressWarnings(value = "unchecked")
abstract public class Modifier {

    protected final PathRef pathHead;
    protected final ModificationContext context;
    protected final Configuration configuration;

    public static Modifier create(PathRef pathHead, ModificationContext context, Configuration configuration) {
        if (ModificationUnit.Operation.ADD.equals(context.getMod().getOperation()))
            return new AddModifier(pathHead, context, configuration);
        else if (ModificationUnit.Operation.REMOVE.equals(context.getMod().getOperation()))
            return new RemoveModifier(pathHead, context, configuration);
        else if (ModificationUnit.Operation.REPLACE.equals(context.getMod().getOperation()))
            return new ReplaceModifier(pathHead, context, configuration);
        else
            throw new IllegalArgumentException(context.getMod().getOperation() + " is not supported.");
    }

    protected Modifier(PathRef pathHead, ModificationContext context, Configuration configuration) {
        this.pathHead = pathHead;
        this.context = context;
        this.configuration = configuration;
    }

    void modify() {
        doModification(pathHead, context.getData());
    }

    private void doModification(PathRef pathRef, Object subject) {
        if (pathRef.getPathToken() instanceof PathRoot) {
            doModification(pathRef.getNext(), subject);
            return;
        }

        if (!pathRef.isTail()) {
            if (!this.configuration.getObjectProvider().containsKey(subject, pathRef.getPathToken().queryFreePath())) {
                if (returnOnMissingProperty())
                    return;
                else if (throwExceptionOnMissingProperty())
                    throw new RuntimeException("Missing property [" + pathRef.getPathToken().queryFreePath() + "] on subject");
                else
                    handleMissingProperty(pathRef, subject);
            }

            Object nextSubject = pathRef.getPathToken().evaluate(subject, this.configuration);
            doModification(pathRef.getNext(), nextSubject);
        } else {
            handleModification(pathRef, subject, this.context.getMod().getValue());
        }
    }

    boolean returnOnMissingProperty() {
        return false;
    }

    boolean throwExceptionOnMissingProperty() {
        return false;
    }

    void handleMissingProperty(PathRef pathRef, Object subject) {

    }

    abstract void handleModification(PathRef pathRef, Object subject, Object value);

    /**
     * A modifier that supports {@link com.kumiq.identity.scim.path.ModificationUnit.Operation#ADD}.
     * If an intermediate node does not exist, it will try to create it
     */
    public static class AddModifier extends Modifier {

        public AddModifier(PathRef pathHead, ModificationContext context, Configuration configuration) {
            super(pathHead, context, configuration);
        }

        @Override
        void handleMissingProperty(PathRef pathRef, Object subject) {
            Schema.Attribute attribute = pathRef.getAttribute(this.context.getSchema());

            if (pathRef.getPathToken() instanceof SimplePathToken) {
                this.configuration.getObjectProvider().createMissingPropertyStructure(
                        subject,
                        pathRef.getPathToken().queryFreePath(),
                        attribute);
            } else {
                throw new IllegalStateException("Impossible state: accessing missing property with index");
            }
        }

        @Override
        void handleModification(PathRef pathRef, Object subject, Object value) {
            Schema.Attribute attribute = pathRef.getAttribute(this.context.getSchema());
            if (attribute.isMultiValued()) {
                if (pathRef.getPathToken().isPathWithIndex()) {
                    throw new IllegalStateException("impossible state: path with index corresponds to multi-valued object");
                } else {
                    Object array = this.configuration.getObjectProvider().getPropertyValue(subject, pathRef.getPathToken().queryFreePath());
                    Assert.isTrue(TypeUtils.isList(array));
                    if (TypeUtils.isCollection(value)) {
                        this.configuration.getObjectProvider().addToArray(array, TypeUtils.asCollection(value));
                    } else {
                        this.configuration.getObjectProvider().addToArray(array, Arrays.asList(value));
                    }
                }
            } else {
                if (TypeUtils.isCollection(value)) {
                    throw new RuntimeException("cannot set collection value unto single valued attribute");
                } else {
                    this.configuration.getObjectProvider().setPropertyValue(subject, pathRef.getPathToken().pathFragment(), value);
                }
            }
        }
    }

    /**
     * A modifier that supports {@link com.kumiq.identity.scim.path.ModificationUnit.Operation#REPLACE}.
     * If an intermediate node does not exist, it will try to throw exception based on the setting
     * {@link com.kumiq.identity.scim.path.Configuration.Option#SUPPRESS_EXCEPTION}.
     */
    public static class ReplaceModifier extends Modifier {

        public ReplaceModifier(PathRef pathHead, ModificationContext context, Configuration configuration) {
            super(pathHead, context, configuration);
        }

        @Override
        boolean throwExceptionOnMissingProperty() {
            return true;
        }

        @Override
        void handleModification(PathRef pathRef, Object subject, Object value) {
            if (pathRef.getPathToken().isPathWithIndex()) {
                PathWithIndexToken token = (PathWithIndexToken) pathRef.getPathToken();
                Object array = this.configuration.getObjectProvider().getPropertyValue(subject, token.getPathComponent());
                this.configuration.getObjectProvider().setArrayIndex(array, token.getIndexComponent(), value);
            } else if (pathRef.getPathToken() instanceof SimplePathToken) {
                this.configuration.getObjectProvider().setPropertyValue(subject, pathRef.getPathToken().pathFragment(), value);
            }
        }
    }

    /**
     * A modifier that supports {@link com.kumiq.identity.scim.path.ModificationUnit.Operation#REMOVE}.
     * If an intermediate node does not exist, it simply returns
     */
    public static class RemoveModifier extends Modifier {

        public RemoveModifier(PathRef pathHead, ModificationContext context, Configuration configuration) {
            super(pathHead, context, configuration);
        }

        @Override
        boolean returnOnMissingProperty() {
            return true;
        }

        @Override
        void handleModification(PathRef pathRef, Object subject, Object value) {
            if (pathRef.getPathToken().isPathWithIndex()) {
                PathWithIndexToken token = (PathWithIndexToken) pathRef.getPathToken();
                Object array = this.configuration.getObjectProvider().getPropertyValue(subject, token.getPathComponent());
                this.configuration.getObjectProvider().removeFromArray(array, token.getIndexComponent());
            } else if (pathRef.getPathToken() instanceof SimplePathToken) {
                this.configuration.getObjectProvider().removePropertyValue(subject, pathRef.getPathToken().pathFragment());
            }
        }
    }
}
