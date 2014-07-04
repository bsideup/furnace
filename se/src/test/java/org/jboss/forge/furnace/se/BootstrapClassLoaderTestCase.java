/*
 * Copyright 2014 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.furnace.se;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Set;
import java.util.concurrent.Future;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.proxy.ClassLoaderAdapterBuilder;
import org.jboss.forge.furnace.repositories.AddonRepositoryMode;
import org.jboss.forge.furnace.util.AddonFilters;
import org.junit.Assert;
import org.junit.Test;

public class BootstrapClassLoaderTestCase
{
   @Test
   public void shouldBeAbleToLoadEnvironment() throws Exception
   {
      final BootstrapClassLoader cl = new BootstrapClassLoader("bootpath");
      Class<?> bootstrapType = cl.loadClass("org.jboss.forge.furnace.impl.FurnaceImpl");
      Method method = bootstrapType.getMethod("startAsync", new Class<?>[] { ClassLoader.class });
      Object result = method.invoke(bootstrapType.newInstance(), cl);
      Assert.assertTrue(result instanceof Future);
   }

   @Test(expected = IllegalStateException.class)
   public void shouldBeAbleToUseFactoryDelegateTypesafely() throws Exception
   {
      Furnace instance = FurnaceFactory.getInstance();
      Assert.assertNotNull(instance);
      AddonRegistry registry = instance.getAddonRegistry();
      Assert.assertNotNull(registry);
   }

   @Test
   public void shouldBeAbleToPassPrimitivesIntoDelegate() throws Exception
   {
      Furnace instance = FurnaceFactory.getInstance();
      Assert.assertNotNull(instance);
      instance.setServerMode(false);
   }

   @Test
   public void shouldBeAbleToPassClassesIntoDelegate() throws Exception
   {
      Furnace instance = FurnaceFactory.getInstance();
      File tempDir = File.createTempFile("test", "repository");
      tempDir.delete();
      tempDir.mkdir();
      tempDir.deleteOnExit();
      instance.addRepository(AddonRepositoryMode.IMMUTABLE, tempDir);
      instance.getRepositories().get(0).getAddonResources(AddonId.from("a", "1"));
   }

   @Test(expected = IllegalStateException.class)
   public void shouldBeAbleToPassInterfacesIntoDelegate() throws Exception
   {
      Furnace instance = FurnaceFactory.getInstance();
      Set<Addon> addons = instance.getAddonRegistry().getAddons(AddonFilters.allStarted());
      Assert.assertNotNull(addons);
   }

   @Test
   public void shouldBeAbleToEnhanceAddonId() throws Exception
   {
      ClassLoader loader = AddonId.class.getClassLoader();
      AddonId enhanced = ClassLoaderAdapterBuilder.callingLoader(loader).delegateLoader(new URLClassLoader(
               new URL[] { new URL("file:///") })).enhance(AddonId.from("a", "1"), AddonId.class);
      Assert.assertNotNull(enhanced);

   }

   @Test
   public void shouldBeAbleToEnhanceAddonIdIntoDelegate() throws Exception
   {
      ClassLoader fromLoader = AddonId.class.getClassLoader();
      ClassLoader toLoader = new URLClassLoader(new URL[] { new URL("file:///") });
      ClassLoaderAdapterBuilder.callingLoader(fromLoader).delegateLoader(toLoader)
               .enhance(AddonId.from("a", "1"), AddonId.class);
   }
}