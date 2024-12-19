package net.nickreuter.branch_coding_exercise.github;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import net.nickreuter.branch_coding_exercise.github.exceptions.RateLimitExceededException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

@RestClientTest(GitHubClient.class)
class GitHubClientTest {

    @Autowired
    private GitHubClient subject;

    @Autowired
    private MockRestServiceServer mockRestServiceServer;

    @TestConfiguration
    static class RestClientTestConfiguration {
        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.build();
        }
    }

    @Test
    void getProfile_WhenProvidedASuccessfulResponse_ReturnsProfile() {
        var expected = Optional.of(new GitHubProfile("octocat", URI.create("https://avatars.githubusercontent.com/u/583231?v=4"), URI.create("https://github.com/octocat"), "The Octocat", "San Francisco", null, Instant.ofEpochSecond(1295981076)));
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new ClassPathResource("/github-example-data/successful-profile.json"), MediaType.APPLICATION_JSON));

        var actual = subject.getProfile("octocat");

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getProfile_WhenGitHubReturns404_ReturnsEmptyOptional() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withResourceNotFound());

        var actual = subject.getProfile("octocat");

        assertThat(actual).isEqualTo(Optional.empty());
    }

    @Test
    void getProfile_WhenGitHubReturns403_ThrowsRateLimitExceededException() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withForbiddenRequest());

        assertThatThrownBy(() -> subject.getProfile("octocat")).isInstanceOf(RateLimitExceededException.class);
    }

    @Test
    void getProfile_WhenGitHubReturns429_ThrowsRateLimitExceededException() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withTooManyRequests());

        assertThatThrownBy(() -> subject.getProfile("octocat")).isInstanceOf(RateLimitExceededException.class);
    }

    @Test
    void getRepos_WhenProvidedASuccessfulResponse_ReturnsListOfRepos() {
        var expected = List.of(new GitHubRepository("carann112", URI.create("https://github.com/octocat3/carann112")), new GitHubRepository("github-slideshow", URI.create("https://github.com/octocat3/github-slideshow")));
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(new ClassPathResource("/github-example-data/successful-repos.json"), MediaType.APPLICATION_JSON));

        var actual = subject.getRepositoriesForUser("octocat");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRepos_WhenGitHubReturnsAnEmptyList_ReturnsEmptyList() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess("[ ]", MediaType.APPLICATION_JSON));

        var actual = subject.getRepositoriesForUser("octocat");
        assertThat(actual).isEmpty();
    }

    @Test
    void getRepos_WhenGitHubReturnsNull_ReturnsEmptyList() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess());

        var actual = subject.getRepositoriesForUser("octocat");
        assertThat(actual).isEmpty();
    }

    @Test
    void getRepos_WhenGitHubReturns403_ThrowsRateLimitExceededException() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withForbiddenRequest());

        assertThatThrownBy(() -> subject.getRepositoriesForUser("octocat")).isInstanceOf(RateLimitExceededException.class);
    }

    @Test
    void getRepos_WhenGitHubReturns429_ThrowsRateLimitExceededException() {
        mockRestServiceServer.expect(requestTo("https://api.github.com/users/octocat/repos"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withTooManyRequests());

        assertThatThrownBy(() -> subject.getRepositoriesForUser("octocat")).isInstanceOf(RateLimitExceededException.class);
    }
}