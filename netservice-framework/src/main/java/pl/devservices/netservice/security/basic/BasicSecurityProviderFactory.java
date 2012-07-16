/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.devservices.netservice.security.basic;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import pl.devservices.netservice.invoker.ClassRegistry;
import pl.devservices.netservice.security.SecurityProfile;
import pl.devservices.netservice.security.SecurityProviderFactory;
import pl.devservices.netservice.security.basic.annotations.UserProfile;

/**
 * Security provider factory. This factor creates simple security provider
 * alowing the netservice to verify if the given method is alowed or not by the
 * system.
 *
 * This provider broses all classes withing classes registry and checks against
 * UserProfile annotations. Follow UserProfile annotations the security groups
 * are assigned to particular methods.
 *
 * If given method is not annotated with UserProfile annotation, it is assumed as 
 * available for all logged in users.
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
  * methods that are assigned at least to one profile provided in the profiles
  * array.
  *
  * @param registry
  * @param profiles
  */
 public BasicSecurityProviderFactory(ClassRegistry registry, String... profiles) {
  this.profiles = profiles;
  this.alowedMethods = new HashMap<String, SecuredMethodDescriptor>();
  Set<String> profilesSet = new HashSet<String>(Arrays.asList(profiles));
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

 @Override
 public SecurityProfile getProvider() {
  return new SecurityProfile() {

   @Override
   public SecuredMethodDescriptor isAllowed(final String className, final String methodName) {
    return alowedMethods.get(className + "#" + methodName);
   }

   @Override
   public String[] getUserGroups() {
    return profiles;
   }
  };
 }
}
