package org.springframework.samples.petclinic.system;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import javax.cache.CacheManager;

import org.junit.jupiter.api.Test;

class AiSystemControllerGeneratedTests {

	@Test
	void crashControllerAlwaysThrowsExpectedRuntimeException() {
		CrashController controller = new CrashController();

		assertThatExceptionOfType(RuntimeException.class).isThrownBy(controller::triggerException)
			.withMessageContaining("showcase what happens when an exception is thrown");
	}

	@Test
	void cacheCustomizerRegistersVetsCache() {
		CacheManager cacheManager = mock(CacheManager.class);

		new CacheConfiguration().petclinicCacheConfigurationCustomizer().customize(cacheManager);

		verify(cacheManager).createCache(eq("vets"), any());
	}

}
