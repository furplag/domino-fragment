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

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.seasar.doma.Entity;
import org.seasar.doma.Transient;
import org.seasar.doma.jdbc.entity.NamingType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jp.furplag.sandbox.domino.misc.origin.Conditionally;
import jp.furplag.sandbox.domino.misc.utilize.Jsonizable;
import jp.furplag.sandbox.domino.misc.vars.Where;
import lombok.Getter;

/**
 * a structure of the {@link Entity} which provides ( just simple ) SQL query .
 *
 * @author furplag
 *
 */
@Entity(immutable = true, naming = NamingType.LENIENT_SNAKE_LOWER_CASE)
@Getter
public abstract class Origin implements Jsonizable<Origin>, Conditionally {

  /** storing condition (s) . */
  @JsonIgnore
  @Transient
  private final Map<String, Where<?>> wheres;

  /** storing order-by column (s) . */
  @JsonIgnore
  @Transient
  private final Queue<OrderBy> order;

  protected Origin() {
    this.wheres = new ConcurrentHashMap<>();
    this.order = new ConcurrentLinkedDeque<>();
  }
}
