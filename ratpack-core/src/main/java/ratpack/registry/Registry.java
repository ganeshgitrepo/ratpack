/*
 * Copyright 2013 the original author or authors.
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

package ratpack.registry;

import ratpack.api.Nullable;

import java.util.List;

/**
 * An object registry.
 * <p>
 * Registries are primarily used for inter {@link ratpack.handling.Handler} communication in request processing.
 * The {@link ratpack.handling.Context} object that handlers operate on implements the {@link ratpack.registry.Registry} interface.
 * <pre class="tested">
 * import ratpack.handling.Handler;
 * import ratpack.handling.Context;
 * import ratpack.registry.Registry;
 * import static ratpack.registry.Registries.just;
 *
 * public class Thing {
 *   private final String name
 *   public Thing(String name) { this.name = name; }
 *   public String getName() { return name; }
 * }
 *
 * public class UpstreamHandler implements Handler {
 *   public void handle(Context context) {
 *     context.next(just(new Thing("foo")));
 *   }
 * }
 *
 * public class DownstreamHandler implements Handler {
 *   public void handle(Context context) {
 *     assert context instanceof Registry;
 *     Thing thing = context.get(Thing.class);
 *     context.render(thing.getName());
 *   }
 * }
 *
 * import ratpack.test.handling.HandlingResult;
 * import ratpack.test.handling.RequestFixture;
 *
 * import static ratpack.test.UnitTest.handle;
 * import static ratpack.handling.Handlers.chain;
 * import static ratpack.func.Actions.noop;
 *
 * Handler chain = chain(new UpstreamHandler(), new DownstreamHandler());
 * HandlingResult result = handle(chain, noop());
 *
 * assert result.rendered(String.class).equals("foo");
 * </pre>
 * <h5>Thread safety</h5>
 * <p>
 * Registry objects are assumed to be thread safe.
 * No external synchronization is performed around registry access.
 * As registry objects may be used across multiple requests, they should be thread safe.
 * <p>
 * Registries that are <b>created</b> per request however do not need to be thread safe.
 */
public interface Registry {

  /**
   * Provides an object of the specified type, or throws an exception if no object of that type is available.
   *
   * @param type The type of the object to provide
   * @param <O> The type of the object to provide
   * @return An object of the specified type
   * @throws NotInRegistryException If no object of this type can be returned
   */
  <O> O get(Class<O> type) throws NotInRegistryException;

  /**
   * Does the same thing as {@link #get(Class)}, except returns null instead of throwing an exception.
   *
   * @param type The type of the object to provide
   * @param <O> The type of the object to provide
   * @return An object of the specified type, or null if no object of this type is available.
   */
  @Nullable
  <O> O maybeGet(Class<O> type);

  /**
   * Returns all of the objects whose declared type is assignment compatible with the given type.
   *
   * @param type the type of objects to search for
   * @param <O> the type of objects to search for
   * @return All objects of the given type
   */
  <O> List<O> getAll(Class<O> type);

}
