package net.nickreuter.branch_coding_exercise.userprofile.domain;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;

import java.net.URI;
import java.time.Instant;
import java.util.List;

public record UserProfile(
        String user_name,
        String display_name,
        URI avatar,
        String geo_location,
        String email,
        URI url,
        Instant created_at,
        List<CodeRepository> repos
) {
    public static UserProfile fromGitHubProfile(GitHubProfile gitHubProfile, List<GitHubRepository> gitHubRepos) {
        return new UserProfile(
                gitHubProfile.login(),
                gitHubProfile.name(),
                gitHubProfile.avatar_url(),
                gitHubProfile.location(),
                gitHubProfile.email(),
                gitHubProfile.html_url(),
                gitHubProfile.created_at(),
                gitHubRepos.stream().map(CodeRepository::fromGitHubRepository).toList()
        );
    }
}
