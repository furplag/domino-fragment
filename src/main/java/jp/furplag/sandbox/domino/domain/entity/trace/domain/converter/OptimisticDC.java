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

package jp.furplag.sandbox.domino.domain.entity.trace.domain.converter;

import org.seasar.doma.ExternalDomain;
import org.seasar.doma.jdbc.domain.DomainConverter;
import jp.furplag.sandbox.domino.domain.entity.trace.domain.Optimistic;
import jp.furplag.sandbox.trebuchet.Trebuchet;

/**
 * {@link DomainConverter} of {@link Optimistic} .
 *
 * @author furplag
 *
 */
@ExternalDomain
public final class OptimisticDC implements DomainConverter<Optimistic, Integer> {

  /** {@inheritDoc}  */
  @Override
  public Integer fromDomainToValue(Optimistic domain) {
    return Trebuchet.Functions.orNot(domain, (t) -> t.getVersion());
  }

  /** {@inheritDoc} */
  @Override
  public Optimistic fromValueToDomain(Integer value) {
    return Optimistic.of(value);
  }
}
