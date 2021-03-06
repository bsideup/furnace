/*
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Eclipse Public License version 1.0, available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.jboss.forge.furnace.manager.impl;

import org.junit.Test;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 * 
 */
public class ClassesNotLeakedOnClasspathTest
{

   @Test(expected = ClassNotFoundException.class)
   public void test() throws ClassNotFoundException
   {
      Thread.currentThread().getContextClassLoader().loadClass("org.objectweb.asm.tree.ClassNode");
   }

}
