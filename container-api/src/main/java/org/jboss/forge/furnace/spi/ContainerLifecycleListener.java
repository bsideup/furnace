/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */

package org.jboss.forge.furnace.spi;

import java.util.ServiceLoader;

import org.jboss.forge.furnace.Furnace;
import org.jboss.forge.furnace.exception.ContainerException;

/**
 * Listens for actions in a Furnace container
 * 
 * Listeners should be registered using the {@link ServiceLoader} mechanism.
 * 
 * @author <a href="mailto:ggastald@redhat.com">George Gastaldi</a>
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
public interface ContainerLifecycleListener
{
   /**
    * Called before the Furnace container starts
    */
   public void beforeStart(Furnace furnace) throws ContainerException;

   /**
    * Called before the Furnace container scans configuration files
    */
   public void beforeConfigurationScan(Furnace furnace) throws ContainerException;

   /**
    * Called after the Furnace container scans configuration files
    */
   public void afterConfigurationScan(Furnace furnace) throws ContainerException;

   /**
    * Called before the Furnace container stops
    */
   public void beforeStop(Furnace furnace) throws ContainerException;

   /**
    * Called after Furnace container stops
    */
   public void afterStop(Furnace furnace) throws ContainerException;
}
