package net.nickreuter.branch_coding_exercise.github.domain;

import java.net.URL;

public record GitHubRepository(
        String name,
        URL html_url
) {}
