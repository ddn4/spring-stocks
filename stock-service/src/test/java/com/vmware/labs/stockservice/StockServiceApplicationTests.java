package com.vmware.labs.stockservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		classes = { StockServiceApplicationTests.TestStockServiceApplicationTestsConfig.class }
)
class StockServiceApplicationTests {

	@Test
	void contextLoads( ApplicationContext applicationContext ) {

		assertThat( applicationContext.containsBean( "marketStatusListener" ) ).isTrue();
		assertThat( applicationContext.containsBean( "stockUpdateListener" ) ).isTrue();

	}

	@SpringBootApplication
	@Import( TestChannelBinderConfiguration.class )
	static class TestStockServiceApplicationTestsConfig { }

}
