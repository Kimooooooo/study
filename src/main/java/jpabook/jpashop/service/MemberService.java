package jpabook.jpashop.service;

import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    //회원가입
    @Transactional  //쓰기에는 readOnly X
    public Long join(Member member){

        validataDuplidateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validataDuplidateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }
    //회원 전체조회
     //읽기에는 readonly넣고
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    //단건조회

    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);
    }


}
