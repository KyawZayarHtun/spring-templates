package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles(value = "dev")
class SpringApiApplicationTests {

	@Test
	void contextLoads() {
		System.out.println(Integer.MAX_VALUE);
	}

}
