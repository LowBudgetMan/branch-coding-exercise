package net.nickreuter.branch_coding_exercise.userprofile;

import net.nickreuter.branch_coding_exercise.github.domain.GitHubProfile;
import net.nickreuter.branch_coding_exercise.github.domain.GitHubRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.CodeRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;

import java.util.List;

class UserProfileBuilder {
    static UserProfile fromGitHubProfile(GitHubProfile gitHubProfile, List<GitHubRepository> gitHubRepos) {
        return new UserProfile(
                gitHubProfile.login(),
                gitHubProfile.name(),
                gitHubProfile.avatar_url(),
                gitHubProfile.location(),
                gitHubProfile.email(),
                gitHubProfile.html_url(),
                gitHubProfile.created_at(),
                gitHubRepos.stream().map(UserProfileBuilder::fromGitHubRepository).toList()
        );
    }

    private static CodeRepository fromGitHubRepository(GitHubRepository gitHubRepo) {
        return new CodeRepository(
                gitHubRepo.name(),
                gitHubRepo.html_url()
        );
    }
}
