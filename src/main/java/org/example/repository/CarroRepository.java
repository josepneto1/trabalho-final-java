package org.example.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.example.model.Carro;

import java.util.List;

public class CarroRepository {
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("at");
    public EntityManager em = emf.createEntityManager();

    public List<Carro> listar() {
        EntityManager em = emf.createEntityManager();
        List<Carro> carros = em.createQuery("from " + Carro.class.getName()).getResultList();
        em.close();
        return carros;
    }

    public Carro buscar(int id) {
        EntityManager em = emf.createEntityManager();
        Carro carro = em.find(Carro.class, id);
        em.close();
        return carro;
    }

    public void inserir(Carro carro) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.persist(carro);
        em.getTransaction().commit();
        em.close();
    }

    public void alterar(Carro carro) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.merge(carro);
        em.getTransaction().commit();
        em.close();
    }

    public void excluir(Carro carro) {
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        em.remove(em.merge(carro));
        em.getTransaction().commit();
        em.close();
    }
}
