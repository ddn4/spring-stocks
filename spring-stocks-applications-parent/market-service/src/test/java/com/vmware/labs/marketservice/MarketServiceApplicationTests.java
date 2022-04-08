package com.vmware.labs.marketservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
		classes = { MarketServiceApplicationTests.TestApplication.class },
		properties = {
				"spring.profiles.active=context-loads"
		}
)
@DirtiesContext
class MarketServiceApplicationTests {

	@Test
	@DisplayName( "Spring Boot Context Loads" )
	void contextLoads( ApplicationContext applicationContext ) {

		assertThat( applicationContext.containsBean( "candidate" ) ).isTrue();
		assertThat( applicationContext.containsBean( "leaderInitiator" ) ).isTrue();

	}

	@SpringBootApplication
	@Import( TestChannelBinderConfiguration.class )
	@Profile( "context-loads" )
	static class TestApplication { }
	
}
