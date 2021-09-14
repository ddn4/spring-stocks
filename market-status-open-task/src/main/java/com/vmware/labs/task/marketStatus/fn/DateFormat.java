/*
 * Copyright 2020-2020 the original author or authors.
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

package com.vmware.labs.task.marketStatus.fn;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.text.SimpleDateFormat;

/**
 * The annotated String must be a valid {@link SimpleDateFormat} pattern.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR,
		ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {DateFormat.DateFormatValidator.class})
public @interface DateFormat {

	/**
	 * Default message for validation.
	 */
	String DEFAULT_MESSAGE = "";

	String message() default DEFAULT_MESSAGE;

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	class DateFormatValidator implements ConstraintValidator<DateFormat, CharSequence> {

		private String message;

		@Override
		public void initialize(DateFormat constraintAnnotation) {
			this.message = constraintAnnotation.message();
		}

		@Override
		public boolean isValid(CharSequence value, ConstraintValidatorContext context) {
			if (value == null) {
				return true;
			}
			try {
				new SimpleDateFormat(value.toString());
			}
			catch (IllegalArgumentException e) {
				if (DEFAULT_MESSAGE.equals(this.message)) {
					context.disableDefaultConstraintViolation();
					context.buildConstraintViolationWithTemplate(e.getMessage()).addConstraintViolation();
				}
				return false;
			}
			return true;
		}

	}

}
