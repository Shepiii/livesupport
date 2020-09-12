package de.shepiii.livesupport.punish;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.shepiii.livesupport.repository.Repository;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Singleton
public final class PunishRepository extends Repository<Punish> {
  @Inject
  private PunishRepository(EntityManager entityManager) {
    super(entityManager, Punish.class);
  }

  private static final String PUNISHED_ID_FIELD_NAME = "punishedId";
  private static final String END_FIELD_NAME = "end";
  private static final String UN_PUNISHED_FIELD_NAME = "unPunished";

  public void isPunished(UUID id, Consumer<Boolean> consumer) {
    findLatestValidPunish(id, punish -> consumer.accept(punish.isPresent()));
  }

  public void findLatestValidPunish(UUID id, Consumer<Optional<Punish>> consumer) {
    executorService().execute(() -> {
      var session = entityManager().unwrap(Session.class);
      var criteria = session.createCriteria(Punish.class);
      criteria.add(Restrictions.eq(PUNISHED_ID_FIELD_NAME, id.toString()));
      criteria.add(Restrictions.eq(UN_PUNISHED_FIELD_NAME, false));
      criteria.add(Restrictions.gt(END_FIELD_NAME, System.currentTimeMillis()));
      criteria.addOrder(Order.asc("pronounced"));
      consumer.accept(criteria.list().stream().findFirst());
    });
  }

  public void findPunishList(UUID id, Consumer<List<Punish>> consumer) {
    executorService().execute(() -> {
      var session = entityManager().unwrap(Session.class);
      var criteria = session.createCriteria(Punish.class);
      criteria.add(Restrictions.eq(PUNISHED_ID_FIELD_NAME, id.toString()));
      criteria.addOrder(Order.asc("pronounced"));
      consumer.accept(criteria.list());
    });
  }

  public void findValidPunishList(UUID id, Consumer<List<Punish>> consumer) {
    executorService().execute(() -> {
      var session = entityManager().unwrap(Session.class);
      var criteria = session.createCriteria(Punish.class);
      criteria.add(Restrictions.eq(PUNISHED_ID_FIELD_NAME, id.toString()));
      criteria.add(Restrictions.eq(UN_PUNISHED_FIELD_NAME, false));
      consumer.accept(criteria.list());
    });
  }
}
