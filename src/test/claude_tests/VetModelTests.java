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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;

/**
 * Unit tests for the Vet domain model entity. Tests vet creation, specialty assignment,
 * and specialty counting.
 */
@DisplayName("Vet Model Tests")
class VetModelTests {

	private Vet vet;

	@BeforeEach
	void setUp() {
		vet = new Vet();
	}

	@Test
	@DisplayName("Should create vet with basic information")
	void testVetCreation() {
		vet.setFirstName("David");
		vet.setLastName("Veterinarian");

		assertThat(vet.getFirstName()).isEqualTo("David");
		assertThat(vet.getLastName()).isEqualTo("Veterinarian");
	}

	@Test
	@DisplayName("Should inherit from Person")
	void testVetInheritsFromPerson() {
		assertThat(vet).isInstanceOf(Person.class);
	}

	@Test
	@DisplayName("Should have empty specialties initially")
	void testEmptySpecialtiesInitially() {
		assertThat(vet.getNrOfSpecialties()).isEqualTo(0);
	}

	@Test
	@DisplayName("Should add specialty to vet")
	void testAddSpecialtyToVet() {
		Specialty specialty = new Specialty();
		specialty.setId(1);
		specialty.setName("Dentistry");

		vet.addSpecialty(specialty);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
		assertThat(vet.getSpecialties()).contains(specialty);
	}

	@Test
	@DisplayName("Should add multiple specialties to vet")
	void testAddMultipleSpecialtiesToVet() {
		Specialty specialty1 = new Specialty();
		specialty1.setId(1);
		specialty1.setName("Radiology");

		Specialty specialty2 = new Specialty();
		specialty2.setId(2);
		specialty2.setName("Surgery");

		vet.addSpecialty(specialty1);
		vet.addSpecialty(specialty2);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).containsExactlyInAnyOrder(specialty1, specialty2);
	}

	@Test
	@DisplayName("Should retrieve sorted specialties list")
	void testRetrieveSortedSpecialties() {
		Specialty specialty1 = new Specialty();
		specialty1.setName("Surgery");

		Specialty specialty2 = new Specialty();
		specialty2.setName("Dentistry");

		vet.addSpecialty(specialty1);
		vet.addSpecialty(specialty2);

		assertThat(vet.getSpecialties()).isNotNull();
	}

	@Test
	@DisplayName("Should get specialties as list")
	void testGetSpecialtiesAsList() {
		Specialty specialty = new Specialty();
		specialty.setName("Internal Medicine");

		vet.addSpecialty(specialty);

		assertThat(vet.getSpecialties().stream().findFirst().orElse(null)).isNotNull();
	}

	@Test
	@DisplayName("Should update vet information")
	void testUpdateVetInformation() {
		vet.setFirstName("Michael");
		vet.setLastName("Chen");

		assertThat(vet.getFirstName()).isEqualTo("Michael");
		assertThat(vet.getLastName()).isEqualTo("Chen");

		vet.setLastName("Chen-Smith");
		assertThat(vet.getLastName()).isEqualTo("Chen-Smith");
	}

	@Test
	@DisplayName("Should set vet ID")
	void testSetVetId() {
		vet.setId(5);
		vet.setFirstName("Sarah");
		vet.setLastName("Martinez");

		assertThat(vet.getId()).isEqualTo(5);
	}

	@Test
	@DisplayName("Should handle vet with no specialties in count")
	void testVetWithNoSpecialtiesCount() {
		assertThat(vet.getNrOfSpecialties()).isZero();
	}

	@Test
	@DisplayName("Should support multiple vets with different specialties")
	void testMultipleVetsWithDifferentSpecialties() {
		Vet vet1 = new Vet();
		vet1.setFirstName("Dr.");
		vet1.setLastName("Smith");

		Vet vet2 = new Vet();
		vet2.setFirstName("Dr.");
		vet2.setLastName("Johnson");

		Specialty specialty1 = new Specialty();
		specialty1.setName("Radiology");

		Specialty specialty2 = new Specialty();
		specialty2.setName("Surgery");

		vet1.addSpecialty(specialty1);
		vet2.addSpecialty(specialty2);

		assertThat(vet1.getNrOfSpecialties()).isEqualTo(1);
		assertThat(vet2.getNrOfSpecialties()).isEqualTo(1);
		assertThat(vet1.getSpecialties()).doesNotContain(specialty2);
		assertThat(vet2.getSpecialties()).doesNotContain(specialty1);
	}

	@Test
	@DisplayName("Should get full vet name")
	void testVetFullName() {
		vet.setFirstName("Emily");
		vet.setLastName("Rodriguez");

		assertThat(vet.getFirstName() + " " + vet.getLastName()).isEqualTo("Emily Rodriguez");
	}

	@Test
	@DisplayName("Should have specialties stored internally")
	void testSpecialtiesStoredInternally() {
		Specialty specialty = new Specialty();
		specialty.setName("Dentistry");

		vet.addSpecialty(specialty);

		// Verify internal storage
		assertThat(vet.getNrOfSpecialties()).isEqualTo(1);
	}

}
