package net.nickreuter.branch_coding_exercise;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BranchCodingExerciseApplication {

	public static void main(String[] args) {
		SpringApplication.run(BranchCodingExerciseApplication.class, args);
	}

}
