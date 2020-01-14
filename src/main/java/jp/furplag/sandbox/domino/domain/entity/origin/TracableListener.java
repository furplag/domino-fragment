package jp.furplag.sandbox.domino.domain.entity.origin;

import org.apache.commons.lang3.StringUtils;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import jp.furplag.sandbox.reflect.SavageReflection;

public class TracableListener<ENTITY extends Tracable> extends PersistiveListner<ENTITY> {

  /** {@inheritDoc} */
  @Override
  public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
    super.preInsert(entity, context);
    SavageReflection.set(entity, "creator", StringUtils.defaultIfBlank(entity.getCreator(), "System"));
    SavageReflection.set(entity, "modifer", StringUtils.defaultIfBlank(entity.getModifier(), entity.getCreator()));
  }

  /** {@inheritDoc} */
  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
    super.preUpdate(entity, context);
    SavageReflection.set(entity, "modifer", StringUtils.defaultIfBlank(entity.getModifier(), entity.getCreator()));
  }
}
