package net.nickreuter.branch_coding_exercise.github.domain;

import java.net.URI;
import java.time.Instant;

public record GitHubProfile (
        String login,
        URI avatar_url,
        URI html_url,
        String name,
        String location,
        String email,
        Instant created_at
) {
}
