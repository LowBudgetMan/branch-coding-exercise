package net.nickreuter.branch_coding_exercise.userprofile.domain;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;

import java.net.URI;

public record CodeRepository(
   String name,
   URI url
) {
    public static CodeRepository fromGitHubRepository(GitHubRepository gitHubRepo) {
        return new CodeRepository(
                gitHubRepo.name(),
                gitHubRepo.html_url()
        );
    }
}
