package net.nickreuter.branch_coding_exercise.github;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
}
