package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    @Test
    public void 회원가입() throws Exception{
        //given
        Member member= new Member();
        member.setName("kim");
        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberRepository.findOne(savedId));
    }
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_가입() throws Exception{
        //given
        Member member1 = new Member();
        member1.setName("김");

        Member member2 = new Member();
        member2.setName("김");

        //when
        Long saveId = memberService.join(member1);
        Long saveId2= memberService.join(member2);

        //then
        Assert.fail("예외 발생");
    }

}