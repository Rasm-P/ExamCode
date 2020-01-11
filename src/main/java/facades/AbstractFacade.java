package facades;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

public abstract class AbstractFacade<T> {

    private final Class<T> ENTITY_CLASS;

    public AbstractFacade(Class<T> entityClass) {
        this.ENTITY_CLASS = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public void edit(T entity) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

    }

    public void remove(long id) {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.remove(em.find(ENTITY_CLASS,id));
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public T find(Object id) {
        return getEntityManager().find(ENTITY_CLASS, id);
    }

    public List<T> findAll() {
        CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(ENTITY_CLASS));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
