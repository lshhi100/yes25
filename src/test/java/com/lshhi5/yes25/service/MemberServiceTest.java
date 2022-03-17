package com.lshhi5.yes25.service;

import com.lshhi5.yes25.domain.Member;
import com.lshhi5.yes25.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() {
        //given
        Member member = new Member();
        member.setName("memberA");

        //when
        Long saveId = memberService.join(member);
        Member savedMember = memberRepository.findOne(saveId);

        //then
//        em.flush();
        Assertions.assertThat(savedMember).isEqualTo(member);
    }

    @Test
    public void 중복_회원_예외() {

        //given
        Member member1 = new Member();
        member1.setName("memberA");
        Member member2 = new Member();
        member2.setName("memberA");

        //when
        memberService.join(member1);

        //then
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));
    }


}