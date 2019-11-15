/**
 * Copyright (C) 2019+ furplag (https://github.com/furplag)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package jp.furplag.sandbox.domino.misc.utilize;

import java.util.LinkedHashMap;
import java.util.Map;
import com.fasterxml.jackson.core.type.TypeReference;
import jp.furplag.data.json.Jsonifier;
import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * JSON &lt;--&gt; object .
 *
 * @author furplag
 *
 * @param <T> the type which origin under converting
 */
public interface Jsonizable<T> {

  /**
   * shorthand for extract the JSON formatted string of the object .
   *
   * @return JSON string
   */
  default String json() {
    return Jsonifier.serialize(this);
  }

  /**
   * shorthand for extract the parameter of the object .
   *
   * @return {@link Map} ( {@link String} key : {@link Object} value )
   */
  default Map<String, Object> map() {
    return Trebuchet.Functions.orElse(json(), new TypeReference<LinkedHashMap<String, Object>>() {}, (Trebuchet.Functions.Bi<String, TypeReference<LinkedHashMap<String, Object>>, Map<String, Object>>) Jsonifier::deserializeStrictly, () -> new LinkedHashMap<>());
  }

  /**
   * shorthand for duplicate similar object .
   *
   * @param <R> the class which inherited T
   * @param deserializeType the class of object to materialize. may not be null
   * @return an instance of {@code deserializeType}
   */
  default <R extends T> R transduce(Class<R> deserializeType) {
    return Jsonifier.deserialize(json(), deserializeType);
  }

  /**
   * shorthand for duplicate object .
   *
   * @param <R> the class
   * @param deserializeType the class of object to materialize. may not be null
   * @return an instance of {@code deserializeType}
   */
  default <R> R mutate(Class<R> deserializeType, String... excludes) {
    return Jsonifier.deserialize(Jsonifier.serialize(SavageReflection.read(this, excludes)), deserializeType);
  }
}
