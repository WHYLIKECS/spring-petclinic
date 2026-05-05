package org.springframework.samples.petclinic.system;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Locale;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@SpringJUnitConfig(WebConfiguration.class)
class AiWebConfigurationGeneratedTests {

	@Autowired
	private LocaleResolver localeResolver;

	@Autowired
	private LocaleChangeInterceptor localeChangeInterceptor;

	@Test
	void localeInfrastructureUsesEnglishAndLangParameter() {
		MockHttpServletRequest request = new MockHttpServletRequest();

		assertThat(this.localeResolver.resolveLocale(request)).isEqualTo(Locale.ENGLISH);
		assertThat(this.localeChangeInterceptor.getParamName()).isEqualTo("lang");
	}

}
