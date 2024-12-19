package net.nickreuter.branch_coding_exercise.userprofile;

import net.nickreuter.branch_coding_exercise.github.GitHubClient;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;
import net.nickreuter.branch_coding_exercise.userprofile.exceptions.UserProfileNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {

    private final GitHubClient gitHubClient;

    public UserProfileService(GitHubClient gitHubClient) {
        this.gitHubClient = gitHubClient;
    }

    public UserProfile getUserProfile(String username) throws UserProfileNotFoundException {
        var gitHubProfile = gitHubClient.getProfile(username).orElseThrow(UserProfileNotFoundException::new);
        var gitHubRepos = gitHubClient.getRepositoriesForUser(username);
        return UserProfile.fromGitHubProfile(gitHubProfile, gitHubRepos);
    }
}
