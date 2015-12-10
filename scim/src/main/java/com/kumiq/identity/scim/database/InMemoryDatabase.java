package com.kumiq.identity.scim.database;

import com.kumiq.identity.scim.filter.FilterCompiler;
import com.kumiq.identity.scim.filter.Predicate;
import com.kumiq.identity.scim.path.*;
import com.kumiq.identity.scim.resource.core.Resource;
import com.kumiq.identity.scim.resource.group.Group;
import com.kumiq.identity.scim.resource.user.User;
import com.kumiq.identity.scim.utils.ExceptionFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Weinan Qiu
 * @since 1.0.0
 */
abstract public class InMemoryDatabase<T extends Resource> implements ResourceDatabase<T> {

    private Map<String, T> dataStore = new HashMap<>();

    @Override
    public Optional<T> findById(String id) {
        return Optional.ofNullable(dataStore.get(id));
    }

    @Override
    public List<T> findAll() {
        return dataStore.values().stream().collect(Collectors.toList());
    }

    @Override
    public T save(T resource) {
        Assert.notNull(resource.getId());
        return dataStore.put(resource.getId(), resource);
    }

    @Override
    public void delete(T resource) {
        if (resource.getId() != null)
            dataStore.remove(resource.getId());
    }

    public void reset() {
        this.dataStore = new HashMap<>();
    }

    /**
     * User in memory database
     * @param <T>
     */
    public static class UserInMemoryDatabase<T extends User> extends InMemoryDatabase<T> implements UserDatabase<T> {

        private Map<String, T> userNameStore = new HashMap<>();

        @Override
        public void reset() {
            super.reset();
            this.userNameStore = new HashMap<>();
        }

        @Override
        public T save(T resource) {
            super.save(resource);
            if (!StringUtils.isEmpty(resource.getUserName()))
                return userNameStore.put(resource.getUserName(), resource);
            return resource;
        }

        @Override
        public Optional<T> findByUserName(String userName) {
            return Optional.ofNullable(userNameStore.get(userName));
        }

        @Override
        @SuppressWarnings("unchecked")
        public List<T> query(String filter, String sort, boolean ascending) {
            List<T> allResources = findAll();
            if (CollectionUtils.isEmpty(allResources))
                return new ArrayList<>();

            Predicate predicate = FilterCompiler.compile(filter);
            List<T> qualifiedResources = allResources.stream().filter(t -> {
                Configuration configuration = Configuration
                        .withResourceObjectProvider()
                        .withOption(Configuration.Option.COMPILE_WITH_HINT)
                        .withOption(Configuration.Option.SUPPRESS_EXCEPTION);
                return predicate.apply(t, configuration);
            }).collect(Collectors.toList());

            if (CollectionUtils.isEmpty(qualifiedResources))
                return new ArrayList<>();

            if (sort != null) {
                Configuration configuration = Configuration.withResourceObjectProvider();   // TODO use schema?
                List<PathRef> compiledPaths;
                try {
                    compiledPaths = PathCompiler.compile(CompilationContext.create(sort, null), configuration);
                } catch (Exception ex) {
                    throw ExceptionFactory.fail(ex.getMessage());
                }
                if (compiledPaths.size() != 1)
                    throw ExceptionFactory.fail("Invalid sort (compiled to multiple paths)");

                qualifiedResources.sort((o1, o2) -> {
                    EvaluationContext evaluationContext1 = new EvaluationContext(o1);
                    EvaluationContext evaluationContext2 = new EvaluationContext(o2);

                    Object result1 = compiledPaths.get(0)
                            .evaluate(evaluationContext1, configuration)
                            .getCursor();
                    Object result2 = compiledPaths.get(0)
                            .evaluate(evaluationContext2, configuration)
                            .getCursor();

                    if (!(result1 instanceof Comparable) || !(result2 instanceof Comparable))
                        return 0;

                    int comparison = ((Comparable) result1).compareTo(result2);
                    return ascending ? comparison : 0 - comparison;
                });
            }

            return qualifiedResources;
        }
    }

    /**
     * Group in memory database
     * @param <T>
     */
    public static class GroupInMemoryDatabase<T extends Group> extends InMemoryDatabase<T> implements GroupDatabase<T> {

        @Override
        public List<T> findGroupsWithMember(String memberId, boolean transitive) {
            List<T> results = new ArrayList<>();
            findGroupsWithMember(results, memberId, transitive);
            return results;
        }

        private void findGroupsWithMember(List<T> groups, String memberId, boolean transitive) {
            List<T> currentResults = findAll().stream().filter(t -> t.hasMember(memberId)).collect(Collectors.toList());

            for (T group : currentResults) {
                if (!groups.stream().filter(t -> t.hasMember(group.getId())).findAny().isPresent()) {
                    groups.add(group);
                    if (transitive)
                        findGroupsWithMember(groups, group.getId(), true);
                }
            }
        }

        @Override
        public void deleteMember(String memberId) {
            List<T> groupsJoinedByMember = findGroupsWithMember(memberId, true);
            groupsJoinedByMember.forEach(t -> {
                t.getMembers().removeIf(member -> member.getValue().equals(memberId));
                this.save(t);
            });
        }
    }
}
