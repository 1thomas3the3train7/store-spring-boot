package com.example.MYSTORE.PRODUCTS.RepositoryImpl;

import com.example.MYSTORE.PRODUCTS.Model.Category;
import com.example.MYSTORE.PRODUCTS.Model.Tea;
import com.example.MYSTORE.PRODUCTS.Repository.CustomTeaRepository;
import com.example.MYSTORE.SECURITY.Model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


@Service
public class CustomTeaRepositoryImpl implements CustomTeaRepository {
    @PersistenceContext
    private EntityManager em;
    @Override
    @Transactional
    public Tea getLazyTeaById(Long id) {
        Tea tea = em.createQuery("select t from Tea t where t.id =?1",Tea.class)
                .setParameter(1,id)
                .getResultList().stream().findFirst().orElse(null);
        return tea;
    }
    @Override
    @Transactional
    public Tea getEagerTeaCategoryReviewImage(Long id) {
        Tea tea = em.createQuery("select t from Tea t left join fetch t.categories " +
                        "left join fetch t.reviews " +
                        "left join fetch t.teaImages " +
                        "where t.id = ?1",Tea.class)
                .setParameter(1,id)
                .getResultList().stream().findFirst().orElse(null);
        return tea;
    }

    @Override
    @Transactional
    public void saveNewTea(Tea tea) {
        em.persist(tea);
    }

    @Override
    @Transactional
    public Tea getTeaAndCategoryByTeaId(Long id) {
        Tea tea = em.createQuery("select t from Tea t left join fetch t.categories where t.id = ?1",Tea.class)
                .setParameter(1,id)
                .getResultList().stream().findFirst().orElse(null);
        return tea;
    }

    @Override
    @Transactional
    public void TeaClearCategoryById(Long id) {
        em.createNativeQuery("delete from tea_and_category where tea_id = ?1")
                .setParameter(1,id)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void uploadTea(Tea tea) {
        em.createQuery("update Tea t set t.name = ?1, t.about = ?2, t.price = ?3," +
                " t.oldPrice = ?4, t.subname = ?5, t.mainLinkImage = ?6 where t.id = ?7")
                .setParameter(1,tea.getName())
                .setParameter(2,tea.getAbout())
                .setParameter(3,tea.getPrice())
                .setParameter(4,tea.getOldPrice())
                .setParameter(5,tea.getSubname())
                .setParameter(6,tea.getMainLinkImage())
                .setParameter(7,tea.getId())
                .executeUpdate();
    }

    @Override
    @Transactional
    public void TeaClearTeaImageById(Long id) {
        em.createNativeQuery("delete from tea_and_image where tea_id = ?1")
                .setParameter(1,id);
    }

    @Override
    @Transactional
    public void updateTeaAndUser(Tea tea, User user) {
        em.createNativeQuery("insert into user_and_tea (user_id,tea_id) values(?1,?2)")
                .setParameter(1,user.getId())
                .setParameter(2,tea.getId())
                .executeUpdate();
    }
    @Override
    @Transactional
    public void updateTeaAndUser(Long teaId, Long userId) {
        em.createNativeQuery("insert into user_and_tea (user_id,tea_id) values(?1,?2)")
                .setParameter(1,userId)
                .setParameter(2,teaId)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void deleteRelationTeaAndUser(Long teaId, Long userId) {
        em.createNativeQuery("delete from user_and_tea ut where ut.tea_id = ?1 and ut.user_id = ?2")
                .setParameter(1,teaId)
                .setParameter(2,userId)
                .executeUpdate();
    }
}