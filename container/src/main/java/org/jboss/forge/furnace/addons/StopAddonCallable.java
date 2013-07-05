/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.furnace.addons;

import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.forge.furnace.addons.Addon;
import org.jboss.forge.furnace.util.Assert;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class StopAddonCallable implements Callable<Void>
{
   private static final Logger logger = Logger.getLogger(StopAddonCallable.class.getName());

   private AddonStateManager stateManager;
   private Addon addon;

   public StopAddonCallable(AddonStateManager stateManager, Addon addon)
   {
      Assert.notNull(stateManager, "State manager must not be null.");
      Assert.notNull(addon, "Addon to stop must not be null.");

      this.stateManager = stateManager;
      this.addon = addon;
   }

   @Override
   public Void call() throws Exception
   {
      AddonRunnable runnable = stateManager.getRunnableOf(addon);
      try
      {
         if (runnable != null)
         {
            runnable.shutdown();
         }
      }
      catch (Exception e)
      {
         logger.log(Level.WARNING, "Failed to shut down addon " + addon, e);
      }
      finally
      {
         stateManager.cancel(addon);
      }
      return null;
   }

}
