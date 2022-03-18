package net.unit8.kysymys.lesson.domain;

import am.ik.yavi.arguments.*;
import am.ik.yavi.builder.ArgumentsValidatorBuilder;
import lombok.*;
import net.unit8.kysymys.user.domain.UserId;

import java.net.URL;
import java.time.LocalDateTime;

@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Answer {
    public static final Arguments5Validator<AnswerId, Problem, UserId, AnswerRepository, LocalDateTime, Answer> validator = ArgumentsValidatorBuilder.of(Answer::new)
            .builder(b->b._object(Arguments1::arg1, "id", c -> c.notNull()))
            .builder(b->b._object(Arguments2::arg2, "problem", c -> c.notNull()))
            .builder(b->b._object(Arguments3::arg3, "answererId", c -> c.notNull()))
            .builder(b->b._object(Arguments4::arg4, "repository", c -> c.notNull()))
            .builder(b->b._object(Arguments5::arg5, "lastAnsweredAt", c -> c.notNull()))
            .build();

    @Getter
    private final AnswerId id;
    @Getter
    private final Problem problem;
    @Getter
    private final UserId answererId;
    @Getter
    private final AnswerRepository repository;
    @Getter
    private final LocalDateTime lastAnsweredAt;

    public static Answer of(AnswerId id, Problem problem, UserId answererId, AnswerRepository repository, LocalDateTime lastAnsweredAt) {
        return validator.validated(id, problem, answererId, repository, lastAnsweredAt);
    }

    public URL getAnswerUrl() {
        return RepositoryUrlBuilder.url(repository.getUrl())
                .commitHash(repository.getCommitHash())
                .build();
    }
}
