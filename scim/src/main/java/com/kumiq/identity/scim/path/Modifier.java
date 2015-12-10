package com.kumiq.identity.scim.path;

import com.kumiq.identity.scim.resource.misc.Schema;
import com.kumiq.identity.scim.utils.TypeUtils;
import org.springframework.util.Assert;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public void modify() {
        doModification(pathHead, context.getData());
    }

    private void doModification(PathRef pathRef, Object subject) {
        if (pathRef.getPathToken() instanceof PathRoot) {
            doModification(pathRef.getNext(), subject);
            return;
        }

        if (!pathRef.isTail()) {
            if (!containsPath(pathRef, subject)) {
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

    boolean containsPath(PathRef pathRef, Object subject) {
        if (pathRef.getPathToken() instanceof PathRoot)
            return true;
        else if (pathRef.getPathToken().isSimplePath())
            return this.configuration.getObjectProvider().containsKey(subject, pathRef.getPathToken().pathFragment());
        else if (pathRef.getPathToken().isPathWithFilter())
            return false;
        else if (pathRef.getPathToken().isPathWithIndex()) {
            PathWithIndexToken indexToken = (PathWithIndexToken) pathRef.getPathToken();
            Object array = this.configuration.getObjectProvider().getPropertyValue(subject, indexToken.getPathComponent());
            if (array == null)
                return false;
            List list = TypeUtils.asList(array);
            if (indexToken.getIndexComponent() >= list.size())
                return false;
        } else {
            return false;
        }

        return true;
    }

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

            if (pathRef.getPathToken().isSimplePath()) {
                SimplePathToken simplePathToken = (SimplePathToken) pathRef.getPathToken();
                this.configuration.getObjectProvider().createMissingPropertyStructure(
                        subject,
                        simplePathToken.getPathFragment(),
                        attribute);
            } else if (pathRef.getPathToken().isPathWithIndex()) {
                PathWithIndexToken pathWithIndexToken = (PathWithIndexToken) pathRef.getPathToken();
                Object array = this.configuration.getObjectProvider().getPropertyValue(subject, pathWithIndexToken.getPathComponent());
                if (array == null) {
                    //this.configuration.getObjectProvider().setPropertyValue(subject, pathWithIndexToken.getPathComponent(), new ArrayList<>());
                    this.configuration.getObjectProvider().createMissingPropertyStructure(
                            subject,
                            pathWithIndexToken.getPathComponent(),
                            attribute);

                    array = this.configuration.getObjectProvider().getPropertyValue(subject, pathWithIndexToken.getPathComponent());
                }
                this.configuration.getObjectProvider().setArrayIndex(
                        array,
                        pathWithIndexToken.getIndexComponent(),
                        this.configuration.getObjectProvider().createArrayElement(pathWithIndexToken.getPathComponent(), attribute));
            } else {
                throw new IllegalStateException("Impossible state: accessing missing property with index");
            }
        }

        @Override
        void handleModification(PathRef pathRef, Object subject, Object value) {
            Schema.Attribute attribute = pathRef.getAttribute(this.context.getSchema());
            if (attribute.isMultiValued()) {
                if (pathRef.getPathToken().isPathWithIndex()) {
                    if (TypeUtils.isCollection(value))
                        throw new IllegalStateException("impossible state: path with index corresponds to multi-valued object");
                    else {
                        PathWithIndexToken pathWithIndexToken = (PathWithIndexToken) pathRef.getPathToken();
                        if (!this.configuration.getObjectProvider().containsKey(subject, pathWithIndexToken.getPathComponent()))
                            this.configuration.getObjectProvider().setPropertyValue(subject, pathWithIndexToken.getPathComponent(), new ArrayList<>());
                        Object array = this.configuration.getObjectProvider().getPropertyValue(subject, pathWithIndexToken.getPathComponent());
                        this.configuration.getObjectProvider().setArrayIndex(array, pathWithIndexToken.getIndexComponent(), value);
                    }
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
