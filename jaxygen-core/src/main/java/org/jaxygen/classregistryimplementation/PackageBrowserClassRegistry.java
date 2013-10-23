package org.jaxygen.classregistryimplementation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.jaxygen.annotations.NetAPI;
import org.jaxygen.invoker.ClassRegistry;
import org.reflections.Reflections;

/**Utility implementation of the ClassRegistry.
 * This class registry implementation browses for all classes
 * with @NetAPI annotation within the specified package name.
 * 
 * One has to extend the service specific implementation in the
 * web-service module, and pass the implementation name into the classRegistry
 * init-param of your APIBrowser.
 * Not that the concrete implementation of this class requires a constructor
 * without parameters.
 *
 * @author Artur Keska <artur.keska@xdsnet.pl>
 */
public abstract class PackageBrowserClassRegistry implements ClassRegistry {

  private String scannedPackageName;

  protected PackageBrowserClassRegistry(String scannedPackageName) {
    this.scannedPackageName = scannedPackageName;
  }
  
  public List<Class> getRegisteredClasses() {
    List<Class> classes = new ArrayList<Class>();
    Reflections reflections = new Reflections(scannedPackageName);
    Set<Class<?>> annotated = 
               reflections.getTypesAnnotatedWith(NetAPI.class);
    classes.addAll(annotated);
    return classes;
  }

}
