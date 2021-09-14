package com.vmware.labs.task.marketStatus;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
		classes = { MarketStatusApplicationTests.TestApplication.class }
)
@DirtiesContext
class MarketStatusApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpringBootApplication
	@Import( TestChannelBinderConfiguration.class )
	public static class TestApplication { }

}
