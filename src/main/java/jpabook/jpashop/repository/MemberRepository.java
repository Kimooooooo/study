package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;


@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Member member) { em.persist(member);
    }

    public Member findOne(Long id){

        return em.find(Member.class, id);
    } //단건조회

    public List<Member> findAll() {

        return em.createQuery("select m from Member m",Member.class) //jpql사용 모두조회
                .getResultList();
    }
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name",Member.class) //이름을 기준으로 조회
                .setParameter("name",name)
                .getResultList();
    }
}
