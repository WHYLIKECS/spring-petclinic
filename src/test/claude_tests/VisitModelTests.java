/*
 * Copyright 2012-2025 the original author or authors.
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

package org.springframework.samples.petclinic.claude_tests;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.owner.Visit;

/**
 * Unit tests for the Visit domain model entity. Tests visit creation, date management,
 * and description handling.
 */
@DisplayName("Visit Model Tests")
class VisitModelTests {

	private Visit visit;

	@BeforeEach
	void setUp() {
		visit = new Visit();
	}

	@Test
	@DisplayName("Should create visit with date and description")
	void testVisitCreation() {
		LocalDate visitDate = LocalDate.of(2024, 6, 15);
		visit.setDate(visitDate);
		visit.setDescription("Annual checkup");

		assertThat(visit.getDate()).isEqualTo(visitDate);
		assertThat(visit.getDescription()).isEqualTo("Annual checkup");
	}

	@Test
	@DisplayName("Should inherit from BaseEntity")
	void testVisitInheritsFromBaseEntity() {
		assertThat(visit).isInstanceOf(BaseEntity.class);
		assertThat(visit.getId()).isNull();
	}

	@Test
	@DisplayName("Should set visit ID")
	void testSetVisitId() {
		visit.setId(1);
		assertThat(visit.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("Should handle current date as default")
	void testCurrentDateHandling() {
		LocalDate today = LocalDate.now();
		visit.setDate(today);

		assertThat(visit.getDate()).isEqualTo(today);
	}

	@Test
	@DisplayName("Should handle past visit dates")
	void testPastVisitDate() {
		LocalDate pastDate = LocalDate.of(2023, 1, 15);
		visit.setDate(pastDate);

		assertThat(visit.getDate()).isBefore(LocalDate.now());
		assertThat(visit.getDate()).isEqualTo(pastDate);
	}

	@Test
	@DisplayName("Should handle empty description")
	void testEmptyDescription() {
		visit.setDescription("");
		assertThat(visit.getDescription()).isEmpty();
	}

	@Test
	@DisplayName("Should handle null description")
	void testNullDescription() {
		visit.setDescription(null);
		assertThat(visit.getDescription()).isNull();
	}

	@Test
	@DisplayName("Should handle long descriptive text")
	void testLongDescription() {
		String longDescription = "Patient presented with excessive scratching and hair loss. "
				+ "Physical examination revealed dermatitis. Prescribed antibiotic ointment and "
				+ "recommended dietary changes. Follow-up appointment scheduled for 2 weeks.";

		visit.setDescription(longDescription);
		assertThat(visit.getDescription()).isEqualTo(longDescription);
	}

	@Test
	@DisplayName("Should update visit date")
	void testUpdateVisitDate() {
		LocalDate initialDate = LocalDate.of(2024, 1, 15);
		LocalDate updatedDate = LocalDate.of(2024, 2, 20);

		visit.setDate(initialDate);
		assertThat(visit.getDate()).isEqualTo(initialDate);

		visit.setDate(updatedDate);
		assertThat(visit.getDate()).isEqualTo(updatedDate);
	}

	@Test
	@DisplayName("Should update visit description")
	void testUpdateVisitDescription() {
		String initialDesc = "Initial checkup";
		String updatedDesc = "Follow-up examination";

		visit.setDescription(initialDesc);
		assertThat(visit.getDescription()).isEqualTo(initialDesc);

		visit.setDescription(updatedDesc);
		assertThat(visit.getDescription()).isEqualTo(updatedDesc);
	}

	@Test
	@DisplayName("Should handle future visit dates")
	void testFutureVisitDate() {
		LocalDate futureDate = LocalDate.now().plusDays(30);
		visit.setDate(futureDate);

		assertThat(visit.getDate()).isAfter(LocalDate.now());
		assertThat(visit.getDate()).isEqualTo(futureDate);
	}

	@Test
	@DisplayName("Should handle special characters in description")
	void testSpecialCharactersInDescription() {
		String descriptionWithSpecialChars = "Pet has: fever (104°F), vomiting & diarrhea; needs IV fluids.";
		visit.setDescription(descriptionWithSpecialChars);

		assertThat(visit.getDescription()).isEqualTo(descriptionWithSpecialChars);
	}

}
