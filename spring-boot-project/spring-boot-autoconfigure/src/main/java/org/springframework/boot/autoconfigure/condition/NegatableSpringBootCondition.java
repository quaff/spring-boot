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

import java.lang.annotation.Annotation;
import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.ResolvableType;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.Assert;

/**
 * Base of all negatable {@link Condition} implementations used with Spring Boot.
 *
 * @author Yanming Zhou
 */
public abstract class NegatableSpringBootCondition<T extends Annotation> extends SpringBootCondition {

	public static final String NEGATING_ATTRIBUTE_NAME = "negating";

	@Override
	public final boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		boolean result = super.matches(context, metadata);
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(getAnnotationName());
		if (annotationAttributes != null && annotationAttributes.containsKey(NEGATING_ATTRIBUTE_NAME)) {
			if ((Boolean) annotationAttributes.get(NEGATING_ATTRIBUTE_NAME)) {
				result = !result;
			}
		}
		return result;
	}

	protected String getAnnotationName() {
		Class<?> clazz = ResolvableType.forClass(getClass())
			.as(NegatableSpringBootCondition.class)
			.getGeneric()
			.resolve();
		Assert.state(clazz != null, "Type argument of NegatableSpringBootCondition should be present");
		return clazz.getName();
	}

}
