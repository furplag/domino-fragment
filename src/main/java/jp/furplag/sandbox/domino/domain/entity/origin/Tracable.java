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
import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.Column;
import org.seasar.doma.Entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jp.furplag.sandbox.domino.domain.entity.trace.domain.Optimistic;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Created;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Modified;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * a structure of the {@link Entity} which enable to tracking .
 *
 * @author furplag
 *
 */
@Entity(immutable = true)
@EqualsAndHashCode(callSuper = true, of = {"creator", "modifier"})
@Getter
@JsonPropertyOrder({"creator", "modifier", "version", "created", "modified"})
public abstract class Tracable extends Persistive {

  /** a user ( or process ) who created . */
  @Column(updatable = false)
  private final String creator;

  /** a user ( or process ) who modified . */
  private final String modifier;

  /**
   *
   * @param creator a user ( or process ) who created
   * @param modifier a user ( or process ) who modified
   * @param version a version number
   * @param created timestamp of created
   * @param modified last update
   */
  protected Tracable(String creator, String modifier, Integer version, Long created, Long modified) {
    this(creator, modifier, Optimistic.of(version), When.asCreated(created), When.asModified(modified));
  }

  /**
   *
   * @param creator a user ( or process ) who created
   * @param modifier a user ( or process ) who modified
   * @param version a version number represented by the type of {@link Optimistic}
   * @param created timestamp represented by the type of {@link Created}
   * @param modified timestamp represented by the type of {@link Modified}
   */
  @JsonCreator
  @ConstructorProperties({"creator", "modifier", "version", "created", "modified"})
  protected Tracable(String creator, String modifier, Optimistic version, Created created, Modified modified) {
    super(version, created, modified);
    this.creator = creator;
    this.modifier = StringUtils.defaultIfBlank(modifier, creator);
  }
}
