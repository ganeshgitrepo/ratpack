/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.exec;

import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import io.netty.channel.EventLoopGroup;
import ratpack.api.NonBlocking;
import ratpack.func.Action;

import java.util.concurrent.Callable;

/**
 * The exec controller manages the execution of operations.
 */
public interface ExecController {

  /**
   * Provides the current context on the current thread.
   * <p>
   * This method is primarily provided for integration with dependency injection frameworks.
   *
   * @return the current context on the current thread
   * @throws NoBoundContextException if this method is called from a thread that is not performing request processing
   */
  ExecContext getContext() throws NoBoundContextException;

  /**
   * The executor that performs computation.
   *
   * @return the executor that performs computation.
   */
  ListeningScheduledExecutorService getExecutor();

  EventLoopGroup getEventLoopGroup();

  @NonBlocking
  void exec(ExecContext.Supplier execContextSupplier, Action<? super ExecContext> action);

  <T> Promise<T> blocking(Callable<T> callable);

  /**
   * Indicates whether the current thread is managed by this execution controller.
   *
   * @return true if the current thread is managed by this execution controller
   */
  boolean isManagedThread();

  <T> Promise<T> promise(Action<? super Fulfiller<T>> action);

  void onExecFinish(Runnable runnable);

  void shutdown() throws Exception;

}
