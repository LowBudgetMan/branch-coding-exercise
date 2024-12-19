package net.nickreuter.branch_coding_exercise.github.domain;

import java.net.URL;
import java.time.Instant;

public record GitHubProfile (
        String login,
        URL avatar_url,
        URL html_url,
        String name,
        String location,
        String email,
        Instant created_at
) {
}
