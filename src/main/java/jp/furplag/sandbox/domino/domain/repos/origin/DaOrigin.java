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
package jp.furplag.sandbox.domino.domain.repos.origin;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.seasar.doma.jdbc.Config;
import org.seasar.doma.jdbc.builder.SelectBuilder;
import jp.furplag.sandbox.domino.domain.entity.origin.Origin;
import jp.furplag.sandbox.domino.misc.generic.Inspector;
import jp.furplag.sandbox.domino.misc.vars.Var;
import jp.furplag.sandbox.domino.misc.vars.Where;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * a structure of DAO which provides ( just simple ) SQL query .
 *
 * @author furplag
 *
 * @param <T> a type of {@link org.seasar.doma.Entity Entity}
 */
public interface DaOrigin<T extends Origin> {

  /**
   * just a simple SQL like that, &quot;select * from&quot; .
   *
   * @return all rows in table
   */
  default List<T> all() {/* @formatter:off */
    final Inspector<T> inspector = Inspector.of(getType());

    return SelectBuilder.newInstance(config())
      .sql(String.join(" ", "select * from", inspector.getName(), "order by"))
      .sql(inspector.getFields(Inspector.Predicates::isIdentity, false).stream().map((pk) -> inspector.getName(pk)).collect(Collectors.joining(", ", " ", "")))
      .getEntityResultList(getType());
  /* @formatter:off */}

  /**
   * retrieves a {@link Config} object from the {@code provider} parameter .
   *
   * @return the configuration
   */
  default Config config() {
    return Config.get(this);
  }

  /**
   * simple search with conditions which represented by {@link org.seasar.doma.Entity Entity} .
   *
   * @param entity {@link org.seasar.doma.Entity Entity}
   * @param excludeFields field name (s) which excludes from condition
   * @return entities which matches conditions
   */
  default List<T> finder(T entity, String... excludeFields) {
    return Trebuchet.Functions.orElse(entity, (_entity) -> _entity.select(SelectBuilder.newInstance(config()), excludeFields).getEntityResultList(getType()), () -> all());
  }

  /**
   * returns an {@link org.seasar.doma.Entity Entity} if {@link #ident(Origin) ident} has result, or return condition .
   *
   * @param entity
   * @return an {@link org.seasar.doma.Entity Entity} if {@link #ident(Origin) ident} has result, or return condition
   */
  default T getIfPersistencive(T entity) {
    return Optional.ofNullable(ident(entity)).orElse(entity);
  }

  /**
   * reflective access to use entity configuration in SQL ( may be used only internal ) .
   *
   * @return a type of {@link org.seasar.doma.Entity Entity}
   */
  @SuppressWarnings({ "unchecked" })
  default Class<T> getType() {
    return (Class<T>) ((ParameterizedType) Trebuchet.Functions.orNot(getClass().getGenericInterfaces()[0].getTypeName(), Class::forName).getGenericInterfaces()[0]).getActualTypeArguments()[0];
  }

  /**
   * simple search with primary keys which represented by {@link org.seasar.doma.Entity Entity} .
   *
   * @param entity {@link org.seasar.doma.Entity Entity}
   * @return entity which matches conditions
   */
  default T ident(T entity) {
    entity.inspector().getFields(Inspector.Predicates::isIdentity, false).forEach((pk) -> entity.where(Where.of(Var.varOf(entity, pk), Where.Operator.Equal)));

    return Trebuchet.Functions.orNot(entity, (_entity) -> _entity.select(SelectBuilder.newInstance(config()), _entity.inspector().getFields(Inspector.Predicates::isIdentity, false).stream().map((pk) -> _entity.inspector().getName(pk)).toArray(String[]::new)).streamEntity(getType()).findFirst().orElse(null));
  }

  /**
   * simple search with conditions which represented by {@link org.seasar.doma.Entity Entity} .
   *
   * @param entity {@link org.seasar.doma.Entity Entity}
   * @param excludeFields field name (s) which excludes from condition
   * @return entity which matches conditions
   */
  default T oneOf(T entity, String... excludeFields) {
    return Trebuchet.Functions.orNot(entity, (_entity) -> _entity.select(SelectBuilder.newInstance(config()), excludeFields).streamEntity(getType()).findFirst().orElse(null));
  }
}
