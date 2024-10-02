package kr.anacnu.pokemonbe.member;

import kr.anacnu.pokemonbe.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByStudentId(String studentId);
}
