/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package unu.jogja.project.ktpp.coba;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import unu.jogja.project.ktpp.coba.exceptions.NonexistentEntityException;
import unu.jogja.project.ktpp.coba.exceptions.PreexistingEntityException;

/**
 *
 * @author HIKMATFATHURRAHMAN
 */
public class DummyyJpaController implements Serializable {

    public DummyyJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = Persistence.createEntityManagerFactory("unu.jogja_project.ktpp_jar_0.0.1-SNAPSHOTPU");

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public DummyyJpaController() {
    }

    public void create(Dummyy dummyy) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(dummyy);
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDummyy(dummyy.getId()) != null) {
                throw new PreexistingEntityException("Dummyy " + dummyy + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Dummyy dummyy) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            dummyy = em.merge(dummyy);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = dummyy.getId();
                if (findDummyy(id) == null) {
                    throw new NonexistentEntityException("The dummyy with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Dummyy dummyy;
            try {
                dummyy = em.getReference(Dummyy.class, id);
                dummyy.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dummyy with id " + id + " no longer exists.", enfe);
            }
            em.remove(dummyy);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Dummyy> findDummyyEntities() {
        return findDummyyEntities(true, -1, -1);
    }

    public List<Dummyy> findDummyyEntities(int maxResults, int firstResult) {
        return findDummyyEntities(false, maxResults, firstResult);
    }

    private List<Dummyy> findDummyyEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Dummyy.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Dummyy findDummyy(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Dummyy.class, id);
        } finally {
            em.close();
        }
    }

    public int getDummyyCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Dummyy> rt = cq.from(Dummyy.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
