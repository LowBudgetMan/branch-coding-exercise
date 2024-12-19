package net.nickreuter.branch_coding_exercise.userprofile.domain;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileTest {

    @Test
    void fromGitHubProfile_WhenUserHasAllFields_ShouldReturnUserProfile() {
        var gitHubProfile = new GitHubProfile("octocat", URI.create("http://this.is.a/avatar-url"), URI.create("https://this.is.a/profile-url"), "The Octocat", "Detroit", "email@a.place", Instant.now());
        var repo1 = new GitHubRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var repo2 = new GitHubRepository("repo2", URI.create("http://this.is.a/repo/2"));
        var gitHubRepos = List.of(repo1, repo2);
        var expectedRepo1 = new CodeRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var expectedRepo2 = new CodeRepository("repo2", URI.create("http://this.is.a/repo/2"));
        var expectedRepos = List.of(expectedRepo1, expectedRepo2);
        var expected = new UserProfile("octocat", "The Octocat", URI.create("http://this.is.a/avatar-url"), "Detroit", "email@a.place", URI.create("https://this.is.a/profile-url"), gitHubProfile.created_at(), expectedRepos);

        var actual = UserProfile.fromGitHubProfile(gitHubProfile, gitHubRepos);

        assertThat(actual).isEqualTo(expected);
    }

}