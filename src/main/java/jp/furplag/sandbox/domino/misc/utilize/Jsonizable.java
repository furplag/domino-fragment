package jp.furplag.sandbox.domino.misc.utilize;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import jp.furplag.data.json.Jsonifier;
import jp.furplag.function.ThrowableBiFunction;

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
    return ThrowableBiFunction.orElseGet(json(), new TypeReference<LinkedHashMap<String, Object>>() {}, Jsonifier::deserialize, LinkedHashMap::new);
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
}
