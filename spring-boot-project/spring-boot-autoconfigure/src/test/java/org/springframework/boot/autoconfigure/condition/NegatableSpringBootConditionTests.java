/*
 * Copyright 2012-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.autoconfigure.condition;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.junit.jupiter.api.Test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link NegatableSpringBootCondition}.
 *
 * @author Yanming Zhou
 */
class NegatableSpringBootConditionTests {

	@Test
	void notNegating() {
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		assertThat(context.containsBean("foo")).isTrue();
	}

	@Test
	void negating() {
		ApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
		assertThat(context.containsBean("bar")).isFalse();
	}

	@Configuration(proxyBeanMethods = false)
	static class Config {

		@Bean
		@AlwaysTrueConditional
		String foo() {
			return "bean";
		}

		@Bean
		@AlwaysTrueConditional(negating = true)
		String bar() {
			return "bean";
		}

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Conditional(AlwaysTrueCondition.class)
	public @interface AlwaysTrueConditional {

		boolean negating() default false;

	}

	static class AlwaysTrueCondition extends NegatableSpringBootCondition<AlwaysTrueConditional> {

		@Override
		public ConditionOutcome getMatchOutcome(ConditionContext context, AnnotatedTypeMetadata metadata) {
			return ConditionOutcome.match();
		}

	}

}
