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

package jp.furplag.sandbox.domino.domain.entity.trace.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.Objects;
import org.seasar.doma.ExternalDomain;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jp.furplag.sandbox.domino.misc.DomainsAware;
import jp.furplag.sandbox.domino.misc.utilize.Jsonizable;
import jp.furplag.sandbox.trebuchet.Trebuchet;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * {@link ExternalDomain} object of the version number which controls optimistic rejection .
 *
 * @author furplag
 *
 */
@DomainsAware(value = "version", type = Integer.class)
@EqualsAndHashCode(of = { "version" })
public class Optimistic implements Comparable<Optimistic>, Jsonizable<Optimistic>, Serializable {

  /** the version number . */
  @Getter
  @JsonProperty("version")
  private final Integer version;

  /**
   *
   * @param version the version number. non negative .
   */
  @ConstructorProperties({ "version" })
  private Optimistic(Integer version) {
    this.version = Trebuchet.Functions.orElse(version, (t) -> t < 0 ? 0 : t, () -> 0);
  }

  /**
   * create an instance of {@link Optimistic} .
   *
   * @param version version number
   * @return {@link Optimistic}
   */
  @JsonCreator
  public static Optimistic of(@JsonProperty("version") final Integer version) {
    return new Optimistic(version);
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return this.json();
  }

  /**
   * {@link #getVersion()} againsts NPE .
   *
   * @return the value of {@link #version}, or {@link Integer#MIN_VALUE} if {@link #version} is null
   */
  private final int intValue() {
    return Objects.requireNonNullElse(version.intValue(), -1);
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(Optimistic anotherOne) {
    return anotherOne == null ? 1 : Integer.compare(intValue(), anotherOne.intValue());
  }
}
