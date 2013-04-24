package org.ratpackframework.bootstrap;

import com.google.inject.Module;
import org.ratpackframework.Action;

import java.util.List;

public interface ModuleRegistry {

  void register(Module instance);

  <T> T get(Class<T> moduleType);

  <T> T get(Class<T> moduleType, Action<? super T> configurer);

  <T extends Module> T remove(Class<T> moduleType);

  List<Module> getModules();

}