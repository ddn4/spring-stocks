package com.vmware.labs.marketService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
		classes = { MarketServiceApplicationTests.TestApplication.class }
)
@DirtiesContext
class MarketServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpringBootApplication
	@Import( TestChannelBinderConfiguration.class )
	public static class TestApplication { }
	
}
