package com.example;

import com.example.model.service.user.UserService;
import com.example.util.payload.dto.user.UserDetailDtoForSecurity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("dev")
class SpringWebApplicationTests {

	@Autowired
	private UserService userService;

	@Test
	void contextLoads() {
	}

}
