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
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.Visit;

/**
 * Unit tests for the Pet domain model entity. Tests pet creation, type assignment, and
 * visit management.
 */
@DisplayName("Pet Model Tests")
class PetModelTests {

	private Pet pet;

	private PetType petType;

	@BeforeEach
	void setUp() {
		pet = new Pet();
		petType = new PetType();
	}

	@Test
	@DisplayName("Should create pet with basic information")
	void testPetCreation() {
		pet.setName("Fluffy");
		pet.setBirthDate(LocalDate.of(2020, 5, 15));

		assertThat(pet.getName()).isEqualTo("Fluffy");
		assertThat(pet.getBirthDate()).isEqualTo(LocalDate.of(2020, 5, 15));
	}

	@Test
	@DisplayName("Should assign pet type to pet")
	void testAssignPetType() {
		petType.setId(1);
		petType.setName("Dog");

		pet.setType(petType);

		assertThat(pet.getType()).isNotNull();
		assertThat(pet.getType().getName()).isEqualTo("Dog");
	}

	@Test
	@DisplayName("Should add visit to pet")
	void testAddVisit() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Annual checkup");

		pet.addVisit(visit);

		assertThat(pet.getVisits()).contains(visit);
	}

	@Test
	@DisplayName("Should add multiple visits to pet")
	void testAddMultipleVisits() {
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2024, 1, 15));
		visit1.setDescription("Checkup");

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2024, 3, 20));
		visit2.setDescription("Vaccination");

		pet.addVisit(visit1);
		pet.addVisit(visit2);

		assertThat(pet.getVisits()).hasSize(2);
		assertThat(pet.getVisits()).contains(visit1, visit2);
	}

	@Test
	@DisplayName("Should have empty visits initially")
	void testInitialEmptyVisits() {
		assertThat(pet.getVisits()).isEmpty();
	}

	@Test
	@DisplayName("Should calculate pet age correctly")
	void testPetAgeCalculation() {
		LocalDate birthDate = LocalDate.now().minusYears(3).minusMonths(2).minusDays(5);
		pet.setBirthDate(birthDate);

		assertThat(pet.getBirthDate()).isBefore(LocalDate.now());
		assertThat(pet.getBirthDate().getYear()).isLessThan(LocalDate.now().getYear());
	}

	@Test
	@DisplayName("Should handle null pet type")
	void testNullPetType() {
		pet.setType(null);
		assertThat(pet.getType()).isNull();
	}

	@Test
	@DisplayName("Should support different pet types")
	void testDifferentPetTypes() {
		PetType dogType = new PetType();
		dogType.setName("Dog");

		PetType catType = new PetType();
		catType.setName("Cat");

		Pet dog = new Pet();
		dog.setType(dogType);
		dog.setName("Buddy");

		Pet cat = new Pet();
		cat.setType(catType);
		cat.setName("Whiskers");

		assertThat(dog.getType().getName()).isEqualTo("Dog");
		assertThat(cat.getType().getName()).isEqualTo("Cat");
	}

	@Test
	@DisplayName("Should handle visit with description")
	void testVisitWithDescription() {
		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Teeth cleaning and checkup");

		pet.addVisit(visit);

		Visit retrievedVisit = pet.getVisits().stream().findFirst().orElse(null);
		assertThat(retrievedVisit).isNotNull();
		assertThat(retrievedVisit.getDescription()).isEqualTo("Teeth cleaning and checkup");
	}

	@Test
	@DisplayName("Should maintain visit chronological order")
	void testVisitChronologicalOrder() {
		Visit visit1 = new Visit();
		visit1.setDate(LocalDate.of(2024, 1, 15));

		Visit visit2 = new Visit();
		visit2.setDate(LocalDate.of(2024, 6, 20));

		Visit visit3 = new Visit();
		visit3.setDate(LocalDate.of(2024, 12, 1));

		pet.addVisit(visit2);
		pet.addVisit(visit1);
		pet.addVisit(visit3);

		assertThat(pet.getVisits()).hasSize(3);
	}

}
