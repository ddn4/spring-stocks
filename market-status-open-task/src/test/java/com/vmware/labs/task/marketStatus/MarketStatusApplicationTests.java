package com.vmware.labs.task.marketStatus;

import com.vmware.labs.task.marketStatus.config.MarketStatusConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.cloud.stream.config.BindingServiceConfiguration;
import org.springframework.context.annotation.Import;

@SpringBootTest
class MarketStatusApplicationTests {

	@Test
	void contextLoads() {
	}

	@SpringBootApplication
	@Import({ MarketStatusConfiguration.class, TestChannelBinderConfiguration.class, BindingServiceConfiguration.class })
	public static class TestApplication { }

}
