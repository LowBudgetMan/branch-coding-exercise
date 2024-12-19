package net.nickreuter.branch_coding_exercise.userprofile.dto;

import net.nickreuter.branch_coding_exercise.userprofile.domain.CodeRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class UserProfileDtoTest {

    @Test
    void fromUserProfile_WhenPassedValidProfile_ConvertsInstantToFormattedString() {
        var expectedRepo1 = new CodeRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var expectedRepo2 = new CodeRepository("repo2", URI.create("http://this.is.a/repo/2"));
        var expectedRepos = List.of(expectedRepo1, expectedRepo2);
        var createdInstant = Instant.ofEpochMilli(1000000000);
        var userProfile = new UserProfile("octocat", "The Octocat", URI.create("http://this.is.a/avatar-url"), "Detroit", "email@a.place", URI.create("https://this.is.a/profile-url"), createdInstant, expectedRepos);
        var expected = new UserProfileDto("octocat", "The Octocat", URI.create("http://this.is.a/avatar-url"), "Detroit", "email@a.place", URI.create("https://this.is.a/profile-url"), "1970-01-12 13:46:40", expectedRepos);

        var actual = UserProfileDto.fromUserProfile(userProfile);

        assertThat(actual).isEqualTo(expected);
    }
}