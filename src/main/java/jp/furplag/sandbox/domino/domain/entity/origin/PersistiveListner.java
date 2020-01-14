package jp.furplag.sandbox.domino.domain.entity.origin;

import java.util.Objects;
import org.seasar.doma.jdbc.entity.EntityListener;
import org.seasar.doma.jdbc.entity.PreInsertContext;
import org.seasar.doma.jdbc.entity.PreUpdateContext;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Created;
import jp.furplag.sandbox.domino.domain.entity.trace.embeddable.When.Modified;
import jp.furplag.sandbox.reflect.SavageReflection;

public abstract class PersistiveListner<ENTITY extends Persistive> implements EntityListener<ENTITY> {

  /** {@inheritDoc} */
  @Override
  public void preInsert(ENTITY entity, PreInsertContext<ENTITY> context) {
    if (Objects.isNull(entity.getCreated())) {
      SavageReflection.set(entity, "created", Created.now());
    }
    if (Objects.isNull(entity.getModified())) {
      SavageReflection.set(entity, "modified", When.asModified(entity.getCreated().getCreated()));
    }
  }

  /** {@inheritDoc} */
  @Override
  public void preUpdate(ENTITY entity, PreUpdateContext<ENTITY> context) {
    if (Objects.isNull(entity.getModified())) {
      SavageReflection.set(entity, "modified", Modified.now());
    }
  }
}
