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
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.vet.Specialty;

/**
 * Unit tests for the Specialty domain model entity. Tests specialty creation, naming, and
 * identification.
 */
@DisplayName("Specialty Model Tests")
class SpecialtyModelTests {

	private Specialty specialty;

	@BeforeEach
	void setUp() {
		specialty = new Specialty();
	}

	@Test
	@DisplayName("Should create specialty with name")
	void testSpecialtyCreation() {
		specialty.setName("Dentistry");
		assertThat(specialty.getName()).isEqualTo("Dentistry");
	}

	@Test
	@DisplayName("Should inherit from NamedEntity")
	void testSpecialtyInheritsFromNamedEntity() {
		assertThat(specialty).isInstanceOf(NamedEntity.class);
	}

	@Test
	@DisplayName("Should set specialty ID")
	void testSetSpecialtyId() {
		specialty.setId(1);
		specialty.setName("Surgery");

		assertThat(specialty.getId()).isEqualTo(1);
		assertThat(specialty.getName()).isEqualTo("Surgery");
	}

	@Test
	@DisplayName("Should support common specialty types")
	void testCommonSpecialtyTypes() {
		String[] specialties = { "Dentistry", "Radiology", "Surgery", "Internal Medicine", "Cardiology" };

		for (String specialtyName : specialties) {
			Specialty spec = new Specialty();
			spec.setName(specialtyName);
			assertThat(spec.getName()).isEqualTo(specialtyName);
		}
	}

	@Test
	@DisplayName("Should update specialty name")
	void testUpdateSpecialtyName() {
		specialty.setName("Orthopedics");
		assertThat(specialty.getName()).isEqualTo("Orthopedics");

		specialty.setName("Neurology");
		assertThat(specialty.getName()).isEqualTo("Neurology");
	}

	@Test
	@DisplayName("Should handle case sensitive specialty names")
	void testCaseSensitiveSpecialtyNames() {
		Specialty spec1 = new Specialty();
		spec1.setName("Surgery");

		Specialty spec2 = new Specialty();
		spec2.setName("surgery");

		assertThat(spec1.getName()).isNotEqualTo(spec2.getName());
	}

	@Test
	@DisplayName("Should return string representation of specialty")
	void testToString() {
		specialty.setName("Cardiology");
		assertThat(specialty.toString()).contains("Cardiology");
	}

	@Test
	@DisplayName("Should handle null name")
	void testNullName() {
		specialty.setName(null);
		assertThat(specialty.getName()).isNull();
	}

	@Test
	@DisplayName("Should create different specialty instances")
	void testMultipleSpecialtyInstances() {
		Specialty spec1 = new Specialty();
		spec1.setId(1);
		spec1.setName("Dentistry");

		Specialty spec2 = new Specialty();
		spec2.setId(2);
		spec2.setName("Radiology");

		assertThat(spec1.getId()).isNotEqualTo(spec2.getId());
		assertThat(spec1.getName()).isNotEqualTo(spec2.getName());
	}

	@Test
	@DisplayName("Should identify specialty by ID and name")
	void testSpecialtyIdentification() {
		specialty.setId(5);
		specialty.setName("Emergency Medicine");

		assertThat(specialty.getId()).isEqualTo(5);
		assertThat(specialty.getName()).isEqualTo("Emergency Medicine");
	}

	@Test
	@DisplayName("Should handle specialty with spaces")
	void testSpecialtyWithSpaces() {
		specialty.setName("Internal Medicine");
		assertThat(specialty.getName()).isEqualTo("Internal Medicine");
	}

	@Test
	@DisplayName("Should support empty specialty name")
	void testEmptySpecialtyName() {
		specialty.setName("");
		assertThat(specialty.getName()).isEmpty();
	}

}
