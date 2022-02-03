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

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import jp.furplag.data.json.Jsonifier;
import jp.furplag.sandbox.reflect.Reflections;
import jp.furplag.sandbox.reflect.SavageReflection;
import jp.furplag.sandbox.stream.Streamr;
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
    return Jsonifier.serializeBrutaly(this);
  }

  /**
   * shorthand for extract the parameter of the object .
   *
   * @return {@link Map} ( {@link String} key : {@link Object} value )
   */
  default Map<String, Object> map() {
    return SavageReflection.read(this, Streamr.stream(Reflections.getFields(this)).filter(Reflections::isStatic).map(Field::getName).toArray(String[]::new));
  }

  /**
   * shorthand for duplicate object .
   *
   * @param <R> the type of object to materialize
   * @param deserializeType the class of object to materialize, may not be null
   * @return an instance of {@code deserializeType}
   */
  default <R> R transduce(Class<R> deserializeType, String... excludeFieldNames) {
    return Jsonifier.deserialize(json(), deserializeType);
  }

  @SuppressWarnings({"unchecked"})
  default <U extends Jsonizable<?>> T merge(U source, String... excludeFieldNames) {/* @formatter:off */
    Trebuchet.Consumers.orNot(
        source, Streamr.collect(HashSet::new, excludeFieldNames)
      , (_source, excludes) -> Streamr.Filter.filtering(_source.map().entrySet(), (entry) -> !excludes.contains(entry.getKey())).forEach((e) -> SavageReflection.set(this, e.getKey(), e.getValue()))
    );
    /* @formatter:on */
    return (T) this;
  }
}
