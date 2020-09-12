package de.shepiii.livesupport.repository;

import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Repository<T> {
  private final ExecutorService executorService = Executors.newCachedThreadPool();
  private final EntityManager entityManager;
  private final Class<T> tClass;

  protected Repository(EntityManager entityManager, Class<T> tClass) {
    this.entityManager = entityManager;
    this.tClass = tClass;
  }

  public void saveOrUpdate(T entity) {
    entityManager.getTransaction().begin();
    entityManager.merge(entity);
    entityManager.getTransaction().commit();
  }

  public void findById(Long id, Consumer<T> consumer) {
    executorService.execute(() -> {
      var entity = entityManager.find(tClass, id);
      if (entity != null) {
        entityManager.detach(entity);
      }
      consumer.accept(entity);
    });
  }

  public void findByIdNullable(Long id, Consumer<Optional<T>> consumer) {
    findById(id, t -> consumer.accept(Optional.ofNullable(t)));
  }

  public ExecutorService executorService() {
    return executorService;
  }

  public EntityManager entityManager() {
    return entityManager;
  }
}
