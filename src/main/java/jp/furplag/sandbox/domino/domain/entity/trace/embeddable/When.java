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

package jp.furplag.sandbox.domino.domain.entity.trace.embeddable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import org.seasar.doma.Column;
import org.seasar.doma.Embeddable;
import org.seasar.doma.Transient;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jp.furplag.sandbox.domino.misc.utilize.Jsonizable;
import jp.furplag.time.Millis;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * stereotype of the timestamp field of an entity .
 *
 * @author furplag
 *
 */
@EqualsAndHashCode(of = { "millis" }, doNotUseGetters = true)
public abstract class When implements Comparable<When>, Jsonizable<When>, Serializable {

  /**
   * meant created timestamp .
   *
   * @author furplag
   *
   */
  @Embeddable
  @EqualsAndHashCode(callSuper = true, of = {})
  @Getter
  @JsonPropertyOrder({ "createdAt", "millis" })
  public static final class Created extends When {

    /** an alias of millis . */
    @Column( updatable = false )
    @JsonIgnore
    private final Long created;

    /**
     *
     * @param created the epoch millis
     */
    @JsonCreator
    protected Created(@JsonProperty("millis") Long created) {
      super(created);
      this.created = super.getMillis();
    }

    /**
     * create an instance of {@link Created} .
     *
     * @return {@link Created} of current millis
     */
    public static Created now() {
      return new When(null) {}.transduce(Created.class);
    }

    /**
     * returns timestamp represented by {@link LocalDateTime} .
     *
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getCreatedAt() {
      return getMoment();
    }
  }

  /**
   * meant modified timestamp .
   *
   * @author furplag
   *
   */
  @Embeddable
  @EqualsAndHashCode(callSuper = true, of = {})
  @Getter
  @JsonPropertyOrder({ "modifiedAt", "millis" })
  public static final class Modified extends When {

    /** an alias of millis . */
    @Column
    @JsonIgnore
    private final Long modified;

    /**
     *
     * @param modified the epoch millis
     */
    @JsonCreator
    protected Modified(@JsonProperty("millis") Long modified) {
      super(modified);
      this.modified = super.getMillis();
    }

    /**
     * create an instance of {@link Created} .
     *
     * @return {@link Modified} of current millis
     */
    public static Modified now() {
      return new When(null) {}.transduce(Modified.class);
    }

    /**
     * returns timestamp represented by {@link LocalDateTime} .
     *
     * @return {@link LocalDateTime}
     */
    public LocalDateTime getModifiedAt() {
      return getMoment();
    }
  }

  /** the epoch millis . */
  @Getter(AccessLevel.PROTECTED)
  @JsonProperty(index = 10_000)
  @Transient
  private final Long millis;

  /** the moment . */
  @Getter(lazy = true)
  @JsonIgnore
  @Transient
  private final LocalDateTime moment = ofEpochMilli();

  /**
   *
   * @param millis the epoch millis
   */
  protected When(Long millis) {
    this.millis = Objects.requireNonNullElseGet(millis, System::currentTimeMillis);
  }

  /**
   * create an instance of {@link Created} .
   *
   * @param created the epoch millis
   * @return {@link Created}
   */
  public static Created asCreated(final Long created) {
    return new Created(created);
  }

  /**
   * create an instance of {@link Modified} .
   *
   * @param modified the epoch millis
   * @return {@link Modified}
   */
  public static Modified asModified(final Long modified) {
    return new Modified(modified);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo(When anotherOne) {
    return Long.valueOf(millis == null ? Long.MIN_VALUE : millis).compareTo(anotherOne == null ? Long.MIN_VALUE : anotherOne.millis);
  }

  /**
   * shorthand for converting an {@link #millis} to {@link LocalDateTime} .
   *
   * @return {@link LocalDateTime}
   */
  private final LocalDateTime ofEpochMilli() {
    return Millis.toLocalDateTime(millis);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return json();
  }
}
