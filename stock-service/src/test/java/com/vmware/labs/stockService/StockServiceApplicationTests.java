package com.vmware.labs.stockService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootTest(
		classes = { StockServiceApplicationTests.TestStockServiceApplicationTestsConfig.class }
)
class StockServiceApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpringBootApplication
	@Import( TestChannelBinderConfiguration.class )
	static class TestStockServiceApplicationTestsConfig { }

}
