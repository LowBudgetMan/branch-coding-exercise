package net.nickreuter.branch_coding_exercise.userprofile;

import net.nickreuter.branch_coding_exercise.github.GitHubClient;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.CodeRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;
import net.nickreuter.branch_coding_exercise.userprofile.exceptions.UserProfileNotFoundException;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserProfileServiceTest {

    private final GitHubClient gitHubClient = mock(GitHubClient.class);
    private final UserProfileService subject = new UserProfileService(gitHubClient);

    @Test
    void getUserProfile_WhenGitHubReturnsDataForProfileAndRepos_ReturnsUserProfileObject() throws UserProfileNotFoundException {
        var gitHubProfile = new GitHubProfile("octocat", URI.create("http://this.is.a/avatar-url"), URI.create("https://this.is.a/profile-url"), "The Octocat", "Detroit", "email@a.place", Instant.now());
        when(gitHubClient.getProfile("octocat")).thenReturn(Optional.of(gitHubProfile));

        var repo1 = new GitHubRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var repo2 = new GitHubRepository("repo2", URI.create("http://this.is.a/repo/2"));
        when(gitHubClient.getRepositoriesForUser("octocat")).thenReturn(List.of(repo1, repo2));

        var expectedRepo1 = new CodeRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var expectedRepo2 = new CodeRepository("repo2", URI.create("http://this.is.a/repo/2"));
        var expected = new UserProfile("octocat", "The Octocat", URI.create("http://this.is.a/avatar-url"), "Detroit", "email@a.place", URI.create("https://this.is.a/profile-url"), gitHubProfile.created_at(), List.of(expectedRepo1, expectedRepo2));

        var actual = subject.getUserProfile("octocat");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserProfile_WhenGitHubReturnsEmptyForProfile_ThrowsUserProfileNotFoundException() {
        when(gitHubClient.getProfile("octocat")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> subject.getUserProfile("octocat")).isInstanceOf(UserProfileNotFoundException.class);
    }

}