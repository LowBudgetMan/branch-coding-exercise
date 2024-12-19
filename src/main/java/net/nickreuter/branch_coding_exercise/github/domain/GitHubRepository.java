package net.nickreuter.branch_coding_exercise.github.domain;

import java.net.URI;

public record GitHubRepository(
        String name,
        URI html_url
) {}
