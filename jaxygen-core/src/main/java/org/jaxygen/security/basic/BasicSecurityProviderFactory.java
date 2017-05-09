/**
 */
package org.jaxygen.security.basic;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jaxygen.invoker.ServiceRegistry;
import org.jaxygen.registry.JaxygenRegistry;
import org.jaxygen.security.SecurityProfile;
import org.jaxygen.security.SecurityProviderFactory;
import org.jaxygen.security.basic.annotations.UserProfile;

/**
 * Security provider factory. This factor creates simple security provider
 * allowing the netservice to verify if the given method is allowed or not by
 * the system.
 *
 * This provider browses all classes within classes registry and checks against
 * UserProfile annotations. Follow UserProfile annotations the security groups
 * are assigned to particular methods.
 *
 * If given method is not annotated with UserProfile annotation, it is assumed
 * as available for all logged in users.
 *
 * @author artur
 */
public class BasicSecurityProviderFactory implements SecurityProviderFactory {

    private Map<String, SecuredMethodDescriptor> alowedMethods;
    private String profiles[];

    public BasicSecurityProviderFactory(List<SecuredMethodDescriptor> alowedMethods, String... profiles) {
        for (SecuredMethodDescriptor sm : alowedMethods) {
            this.alowedMethods.put(sm.getClassName() + "#" + sm.getMethodName(), sm);
            this.profiles = profiles;
        }
    }

    /**
     * Browse all classes in the registry and find all methods annotated with
     * UserProfile annotation. The build SecurityProvider contains a list of
     * methods that are assigned at least to one profile provided in the
     * profiles array.
     *
     * @param registry A class registry managed by this provider.
     * @param profiles A list of available security profiles.
     */
    public BasicSecurityProviderFactory(ServiceRegistry registry, String... profiles) {
        addRegistry(registry, profiles);
    }

    /**
     * Browse all classes in from all registered modules. with UserProfile
     * annotation. The build SecurityProvider contains a list of methods that
     * are assigned at least to one profile provided in the profiles array.
     *
     * @param profiles A list of available security profiles.
     */
    public BasicSecurityProviderFactory(String... profiles) {
        JaxygenRegistry.instance().getClassRegistries().stream()
                .forEach(reg -> addRegistry(reg, profiles));
    }

    @Override
    public SecurityProfile getProvider() {
        return new SecurityProfile() {
            @Override
            public SecuredMethodDescriptor isAllowed(final String className, final String methodName) {
                return alowedMethods.get(className + "#" + methodName);
            }

            @Override
            public String[] getAllowedMethodDescriptors() {
                return alowedMethods.keySet().toArray(new String[alowedMethods.size()]);
            }

            @Override
            public String[] getUserGroups() {
                return profiles;
            }
        };
    }

    private void addRegistry(ServiceRegistry registry, String[] profiles) throws SecurityException {
        this.profiles = profiles;
        this.alowedMethods = new HashMap<>();
        Set<String> profilesSet = new HashSet<>(Arrays.asList(profiles));
        for (Class<?> clazz : registry.getRegisteredClasses()) {
            for (Method m : clazz.getMethods()) {
                UserProfile profile = m.getAnnotation(UserProfile.class);
                boolean found = false;
                if (profile != null) {
                    for (String profileName : profile.name()) {
                        found = profilesSet.contains(profileName);
                        if (found) {
                            break;
                        }
                    }
                } else {
                    found = true;
                }
                if (found) {
                    SecuredMethodDescriptor sm = new SecuredMethodDescriptor(clazz.getCanonicalName(), m.getName());
                    this.alowedMethods.put(sm.getClassName() + "#" + sm.getMethodName(), sm);
                }
            }
        }
    }
}
