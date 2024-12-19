package net.nickreuter.branch_coding_exercise.userprofile.domain;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class CodeRepositoryTest {

    @Test
    void fromGitHubRepository_ReturnsObjectWithCorrectFieldsSet() {
        var gitHubRepository = new GitHubRepository("github repo name", URI.create("http://this.is.a/url"));
        var expected = new CodeRepository("github repo name", URI.create("http://this.is.a/url"));
        var actual = CodeRepository.fromGitHubRepository(gitHubRepository);

        assertThat(actual).isEqualTo(expected);
    }
}