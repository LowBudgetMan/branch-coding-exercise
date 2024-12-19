package net.nickreuter.branch_coding_exercise.userprofile;

import net.nickreuter.branch_coding_exercise.userprofile.domain.CodeRepository;
import net.nickreuter.branch_coding_exercise.userprofile.domain.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.net.URI;
import java.time.Instant;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private final UserProfileService userProfileService = mock(UserProfileService.class);

    @Test
    void getUserProfile_WhenServiceReturnsProfile_Returns200() throws Exception {
        var username = "octocat";
        var expectedRepo1 = new CodeRepository("repo1", URI.create("http://this.is.a/repo/1"));
        var expectedRepo2 = new CodeRepository("repo2", URI.create("http://this.is.a/repo/2"));
        var expectedRepos = List.of(expectedRepo1, expectedRepo2);
        var createdInstant = Instant.ofEpochMilli(1000000000);
        var userProfile = new UserProfile(username, "The Octocat", URI.create("http://this.is.a/avatar-url"), "Detroit", "email@a.place", URI.create("https://this.is.a/profile-url"), createdInstant, expectedRepos);
        when(userProfileService.getUserProfile(username)).thenReturn(userProfile);

        mockMvc.perform(get("/api/user-profile/%s".formatted(username)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user_name").value("octocat"))
                .andExpect(jsonPath("$.display_name").value("The Octocat"))
                .andExpect(jsonPath("$.avatar").value("http://this.is.a/avatar-url"))
                .andExpect(jsonPath("$.geo_location").value("Detroit"))
                .andExpect(jsonPath("$.email").value("email@a.place"))
                .andExpect(jsonPath("$.url").value("https://this.is.a/profile-url"))
                .andExpect(jsonPath("$.created_at").value("1970-01-12 13:46:40"))
                .andExpect(jsonPath("$.repos.[0].name").value("repo1"))
                .andExpect(jsonPath("$.repos.[0].url").value("http://this.is.a/repo/1"))
                .andExpect(jsonPath("$.repos.[1].name").value("repo2"))
                .andExpect(jsonPath("$.repos.[1].url").value("http://this.is.a/repo/2"));
    }
}