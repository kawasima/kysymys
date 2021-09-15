package net.unit8.kysymys.config;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import net.unit8.kysymys.lesson.adapter.persistence.*;
import net.unit8.kysymys.user.adapter.persistence.UserJpaEntity;
import net.unit8.kysymys.user.adapter.persistence.UserRepository;
import net.unit8.kysymys.user.domain.UserId;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class DatabaseSetup implements InitializingBean {
    private final UserRepository userRepository;
    private final ProblemRepository problemRepository;
    private final AnswerRepository answerRepository;
    private final SubmissionRepository submissionRepository;
    private final TransactionTemplate tx;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSetup(UserRepository userRepository, ProblemRepository problemRepository, AnswerRepository answerRepository, SubmissionRepository submissionRepository, TransactionTemplate tx, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.problemRepository = problemRepository;
        this.answerRepository = answerRepository;
        this.submissionRepository = submissionRepository;
        this.tx = tx;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (userRepository.findByEmail("teacher1@example.com").isPresent())
            return;

        UserJpaEntity teacher1 = new UserJpaEntity();
        teacher1.setId(new UserId().getValue());
        teacher1.setName("teacher1");
        teacher1.setEmail("teacher1@example.com");
        teacher1.setPassword(passwordEncoder.encode("password"));
        teacher1.setRoles(Set.of("STUDENT", "TEACHER"));

        UserJpaEntity student1 = new UserJpaEntity();
        student1.setId(new UserId().getValue());
        student1.setName("student1");
        student1.setEmail("student1@example.com");
        student1.setPassword(passwordEncoder.encode("password"));
        student1.setRoles(Set.of("STUDENT"));

        String problem1Id = NanoIdUtils.randomNanoId();
        ProblemJpaEntity problem1 = new ProblemJpaEntity();
        problem1.setId(problem1Id);
        problem1.setName("Problem1");
        problem1.setRepositoryUrl("https://github.com/kawasima/problem1");
        problem1.setBranch("main");

        AnswerJpaEntity answer1 = new AnswerJpaEntity();
        answer1.setId(NanoIdUtils.randomNanoId());
        answer1.setAnswererId(teacher1.getId());
        answer1.setProblem(problem1);
        answer1.setRepositoryUrl("https://github.com/kawasima/answer1");

        SubmissionJpaEntity submission1 = new SubmissionJpaEntity();
        submission1.setId(NanoIdUtils.randomNanoId());
        submission1.setAnswer(answer1);
        submission1.setCommitHash("0123456789012345678901234567890123456789");
        submission1.setSubmittedAt(LocalDateTime.now());
        answer1.setLatestSubmission(submission1);
        tx.execute(status -> {
            userRepository.save(teacher1);
            userRepository.save(student1);
            problemRepository.save(problem1);
            answerRepository.save(answer1);
            return null;
        });
    }
}
