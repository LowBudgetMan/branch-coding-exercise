package net.nickreuter.branch_coding_exercise.github;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Optional;

@Component
public class GitHubClient {

    private static final String GITHUB_BASE_URL = "https://api.github.com";
    private final RestClient restClient;

    public GitHubClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public Optional<GitHubProfile> getProfile(String username) {
        try {
            return Optional.ofNullable(restClient.get()
                .uri("%s/users/%s".formatted(GITHUB_BASE_URL, username))
                .retrieve()
                .onStatus(status -> status.value() == 404, (_, _) -> {throw new IllegalArgumentException();})
                .body(GitHubProfile.class));
        } catch(IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public List<GitHubRepository> getRepositoriesForUser(String username) {
        return restClient.get()
                .uri("%s/users/%s/repos".formatted(GITHUB_BASE_URL, username))
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
