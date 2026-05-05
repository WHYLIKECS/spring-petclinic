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
import org.springframework.samples.petclinic.owner.PetType;

/**
 * Unit tests for the PetType domain model entity. Tests pet type creation, naming, and
 * identification.
 */
@DisplayName("PetType Model Tests")
class PetTypeModelTests {

	private PetType petType;

	@BeforeEach
	void setUp() {
		petType = new PetType();
	}

	@Test
	@DisplayName("Should create pet type with name")
	void testPetTypeCreation() {
		petType.setName("Dog");
		assertThat(petType.getName()).isEqualTo("Dog");
	}

	@Test
	@DisplayName("Should inherit from NamedEntity")
	void testPetTypeInheritsFromNamedEntity() {
		assertThat(petType).isInstanceOf(NamedEntity.class);
	}

	@Test
	@DisplayName("Should set and get pet type ID")
	void testPetTypeId() {
		petType.setId(1);
		petType.setName("Cat");

		assertThat(petType.getId()).isEqualTo(1);
		assertThat(petType.getName()).isEqualTo("Cat");
	}

	@Test
	@DisplayName("Should support common pet types")
	void testCommonPetTypes() {
		String[] petTypes = { "Dog", "Cat", "Bird", "Rabbit", "Hamster", "Snake" };

		for (String typeName : petTypes) {
			PetType type = new PetType();
			type.setName(typeName);
			assertThat(type.getName()).isEqualTo(typeName);
		}
	}

	@Test
	@DisplayName("Should update pet type name")
	void testUpdatePetTypeName() {
		petType.setName("Dog");
		assertThat(petType.getName()).isEqualTo("Dog");

		petType.setName("Puppy");
		assertThat(petType.getName()).isEqualTo("Puppy");
	}

	@Test
	@DisplayName("Should handle case sensitive pet type names")
	void testCaseSensitivePetTypeName() {
		PetType type1 = new PetType();
		type1.setName("Dog");

		PetType type2 = new PetType();
		type2.setName("dog");

		assertThat(type1.getName()).isNotEqualTo(type2.getName());
	}

	@Test
	@DisplayName("Should return string representation of pet type")
	void testToString() {
		petType.setName("Dog");
		assertThat(petType.toString()).contains("Dog");
	}

	@Test
	@DisplayName("Should handle null name")
	void testNullName() {
		petType.setName(null);
		assertThat(petType.getName()).isNull();
	}

	@Test
	@DisplayName("Should create different pet type instances")
	void testMultiplePetTypeInstances() {
		PetType dogType = new PetType();
		dogType.setId(1);
		dogType.setName("Dog");

		PetType catType = new PetType();
		catType.setId(2);
		catType.setName("Cat");

		assertThat(dogType.getId()).isNotEqualTo(catType.getId());
		assertThat(dogType.getName()).isNotEqualTo(catType.getName());
	}

	@Test
	@DisplayName("Should identify pet type by ID and name")
	void testPetTypeIdentification() {
		petType.setId(3);
		petType.setName("Bird");

		assertThat(petType.getId()).isEqualTo(3);
		assertThat(petType.getName()).isEqualTo("Bird");
	}

}
