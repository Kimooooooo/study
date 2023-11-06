package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private  final MemberService memberService;
    @GetMapping("/members/new")
    public String creatForm(Model model){
        model.addAttribute("memberForm",new MemberForm());
        return "members/createMemberForm";
    }
    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result){
        if(result.hasErrors()){ // 만약오류가잇다면
            return"members/createMemberForm";
        }
       Address address = new Address(form.getCity(), form.getStreet() ,form.getZipcode());

       Member member = new Member();
       member.setName(form.getName());
       member.setAddress(address);

       memberService.join(member);
       return "redirect:/";
    }

    @GetMapping("/members")//jpa에서 중요한건 entity는 순수하게 유지해야함 1대1로 엔티티를 사용하면 나중에 핵심비즈니스로직을수정하다 화면이꺠질수잇음
     //dto를이용하는것을 권장
    public String list(Model model){ //api를 만들떄는 이유를 불문하고 엔티티를 넘기면안됨
        model.addAttribute("members", memberService.findMembers());
        return "members/memberList";
    }
}
