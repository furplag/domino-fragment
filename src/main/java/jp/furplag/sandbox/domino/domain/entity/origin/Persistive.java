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
package jp.furplag.sandbox.domino.domain.entity.origin;

import java.beans.ConstructorProperties;
import java.util.Objects;
import java.util.stream.IntStream;
import org.seasar.doma.Entity;
import org.seasar.doma.Version;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jp.furplag.sandbox.domino.domain.entity.trace.domain.Optimistic;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Created;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Modified;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * a structure of the {@link Entity} which enable to control optimistic rejection .
 *
 * @author furplag
 *
 */
@Entity(immutable = true)
@EqualsAndHashCode(callSuper = false, of = { "version", "created", "modified" })
@Getter
@JsonPropertyOrder({ "version", "created", "modified" })
public abstract class Persistive extends Origin implements Comparable<Persistive> {

  /** version number . */
  @Version
  private final Optimistic version;

  /** created timestamp . */
  private final Created created;

  /** modified timestamp . */
  private final Modified modified;

  protected Persistive() {
    this(Optimistic.of(null), null, null);
  }

  /**
   *
   * @param version a version number
   * @param created timestamp of created
   * @param modified last update
   */
  protected Persistive(Integer version, Long created, Long modified) {
    this(Optimistic.of(version), When.asCreated(Objects.nonNull(created) ? created : modified), When.asModified(modified));
  }

  /**
   *
   * @param version a version number represented by the type of {@link Optimistic}
   * @param created timestamp represented by the type of {@link Created}
   * @param modified timestamp represented by the type of {@link Modified}
   */
  @JsonCreator
  @ConstructorProperties({ "version", "created", "modified" })
  protected Persistive(@JsonProperty("version") Optimistic version, @JsonProperty("created") Created created, @JsonProperty("modified") Modified modified) {
    this.version = Objects.requireNonNullElse(version, Optimistic.of(null));
    this.modified = Objects.requireNonNullElseGet(modified, Modified::now);
    this.created = Objects.requireNonNullElse(created, this.modified.transduce(Created.class));
  }

  /** {@inheritDoc} */
  @Override
  public int compareTo(Persistive anotherOne) {
    return anotherOne == null ? 1 : IntStream.of(0, version.compareTo(anotherOne.version), modified.compareTo(anotherOne.modified), created.compareTo(anotherOne.created)).filter((x) -> x != 0).findFirst().orElse(0);
  }

  /** {@inheritDoc} *//* @formatter:off */@Override public String toString() {return json();}/* @formatter:on */
}
