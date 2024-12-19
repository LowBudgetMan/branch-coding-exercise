package net.nickreuter.branch_coding_exercise.userprofile.domain;

import java.net.URI;

public record CodeRepository(
   String name,
   URI url
) {}
