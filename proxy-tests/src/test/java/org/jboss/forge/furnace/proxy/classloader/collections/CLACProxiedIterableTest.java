/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.proxy.classloader.collections;

import java.util.Iterator;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.forge.arquillian.archive.ForgeArchive;
import org.jboss.forge.arquillian.services.LocalServices;
import org.jboss.forge.classloader.mock.IterableFactory;
import org.jboss.forge.furnace.addons.AddonId;
import org.jboss.forge.furnace.addons.AddonRegistry;
import org.jboss.forge.furnace.proxy.ClassLoaderAdapterBuilder;
import org.jboss.forge.furnace.proxy.Proxies;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class CLACProxiedIterableTest
{
   @Deployment(order = 3)
   public static ForgeArchive getDeployment()
   {
      ForgeArchive archive = ShrinkWrap.create(ForgeArchive.class)
               .addBeansXML()
               .addClasses(IterableFactory.class)
               .addAsLocalServices(CLACProxiedIterableTest.class);

      return archive;
   }

   @Deployment(name = "dep,1", testable = false, order = 2)
   public static ForgeArchive getDeploymentDep1()
   {
      ForgeArchive archive = ShrinkWrap.create(ForgeArchive.class)
               .addClasses(IterableFactory.class)
               .addBeansXML();

      return archive;
   }

   @Test
   public void testIterableTypesAreProxied() throws Exception
   {
      AddonRegistry registry = LocalServices.getFurnace(getClass().getClassLoader())
               .getAddonRegistry();
      ClassLoader thisLoader = CLACProxiedIterableTest.class.getClassLoader();
      ClassLoader dep1Loader = registry.getAddon(AddonId.from("dep", "1")).getClassLoader();

      Class<?> foreignType = dep1Loader.loadClass(IterableFactory.class.getName());
      Iterable<?> proxy = (Iterable<?>) foreignType.getMethod("getIterable")
               .invoke(foreignType.newInstance());
      Assert.assertFalse(Proxies.isForgeProxy(proxy));

      Object delegate = foreignType.newInstance();
      IterableFactory enhancedFactory = (IterableFactory) ClassLoaderAdapterBuilder.callingLoader(thisLoader)
               .delegateLoader(dep1Loader).enhance(delegate);

      Assert.assertTrue(Proxies.isForgeProxy(enhancedFactory));
      Iterable<?> enhancedInstance = enhancedFactory.getIterable();
      Assert.assertTrue(Proxies.isForgeProxy(enhancedInstance));

      Iterator<?> iterator = enhancedInstance.iterator();
      Assert.assertNotNull(iterator);
   }

   @Test
   public void testCustomIterableTypesAreProxied() throws Exception
   {
      AddonRegistry registry = LocalServices.getFurnace(getClass().getClassLoader())
               .getAddonRegistry();
      ClassLoader thisLoader = CLACProxiedIterableTest.class.getClassLoader();
      ClassLoader dep1Loader = registry.getAddon(AddonId.from("dep", "1")).getClassLoader();

      Class<?> foreignType = dep1Loader.loadClass(IterableFactory.class.getName());
      Iterable<?> proxy = (Iterable<?>) foreignType.getMethod("getIterable")
               .invoke(foreignType.newInstance());
      Assert.assertFalse(Proxies.isForgeProxy(proxy));

      Object delegate = foreignType.newInstance();
      IterableFactory enhancedFactory = (IterableFactory) ClassLoaderAdapterBuilder.callingLoader(thisLoader)
               .delegateLoader(dep1Loader).enhance(delegate);

      Assert.assertTrue(Proxies.isForgeProxy(enhancedFactory));
      Iterable<?> enhancedInstance = enhancedFactory.getCustomIterable();
      Assert.assertTrue(Proxies.isForgeProxy(enhancedInstance));

      Iterator<?> iterator = enhancedInstance.iterator();
      Assert.assertNotNull(iterator);
   }
}