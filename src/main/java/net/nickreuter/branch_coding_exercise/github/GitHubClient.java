package net.nickreuter.branch_coding_exercise.github;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class GitHubClient {

    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private final RestClient restClient;

    public GitHubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public GitHubProfile getProfile(String username) {
        return restClient.get()
                .uri("%s/users/%s".formatted(GITHUB_BASE_URL, username))
                .retrieve()
                .body(GitHubProfile.class);
    }

    public List<GitHubRepository> getRepositoriesForUser(String username) {
        return restClient.get()
                .uri("%s/users/%s/repos".formatted(GITHUB_BASE_URL, username))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
