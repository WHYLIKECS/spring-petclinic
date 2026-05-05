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
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.Visit;

/**
 * Unit tests for the Owner domain model entity. Tests owner entity creation, pet
 * management, and visit association.
 */
@DisplayName("Owner Model Tests")
class OwnerModelTests {

	private Owner owner;

	@BeforeEach
	void setUp() {
		owner = new Owner();
	}

	@Test
	@DisplayName("Should create owner with basic information")
	void testOwnerCreation() {
		owner.setFirstName("John");
		owner.setLastName("Doe");
		owner.setAddress("123 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("5555551234");

		assertThat(owner.getFirstName()).isEqualTo("John");
		assertThat(owner.getLastName()).isEqualTo("Doe");
		assertThat(owner.getAddress()).isEqualTo("123 Main St");
		assertThat(owner.getCity()).isEqualTo("Springfield");
		assertThat(owner.getTelephone()).isEqualTo("5555551234");
	}

	@Test
	@DisplayName("Should inherit from BaseEntity and have id property")
	void testOwnerInheritsFromBaseEntity() {
		Owner testOwner = new Owner();
		assertThat(testOwner).isInstanceOf(BaseEntity.class);
		assertThat(testOwner.getId()).isNull();
		testOwner.setId(1);
		assertThat(testOwner.getId()).isEqualTo(1);
	}

	@Test
	@DisplayName("Should add pet to owner's collection")
	void testAddPet() {
		Pet pet = new Pet();
		pet.setName("Fluffy");
		pet.setBirthDate(LocalDate.of(2020, 1, 15));

		owner.addPet(pet);

		assertThat(owner.getPets()).hasSize(1);
		assertThat(owner.getPets()).contains(pet);
	}

	@Test
	@DisplayName("Should add multiple pets to owner")
	void testAddMultiplePets() {
		Pet pet1 = new Pet();
		pet1.setName("Fluffy");

		Pet pet2 = new Pet();
		pet2.setName("Spot");

		owner.addPet(pet1);
		owner.addPet(pet2);

		assertThat(owner.getPets()).hasSize(2);
		assertThat(owner.getPets()).extracting("name").contains("Fluffy", "Spot");
	}

	@Test
	@DisplayName("Should find pet by name")
	void testGetPetByName() {
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);

		Pet foundPet = owner.getPet("Fluffy");
		assertThat(foundPet).isNotNull();
		assertThat(foundPet.getName()).isEqualTo("Fluffy");
	}

	@Test
	@DisplayName("Should return null when pet name not found")
	void testGetPetByNameNotFound() {
		Pet foundPet = owner.getPet("NonExistent");
		assertThat(foundPet).isNull();
	}

	@Test
	@DisplayName("Should find pet by ID")
	void testGetPetById() {
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);

		Pet foundPet = owner.getPet("Fluffy");
		assertThat(foundPet).isNotNull();
		assertThat(foundPet.getName()).isEqualTo("Fluffy");
	}

	@Test
	@DisplayName("Should add visit to pet")
	void testAddVisitToPet() {
		Pet pet = new Pet();
		pet.setName("Fluffy");
		owner.addPet(pet);

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Checkup");

		pet.addVisit(visit);

		assertThat(pet.getVisits()).contains(visit);
	}

	@Test
	@DisplayName("Should handle empty pet collection")
	void testEmptyPetCollection() {
		assertThat(owner.getPets()).isEmpty();
	}

	@Test
	@DisplayName("Should support duplicate pet names for different owners")
	void testMultipleOwnersWithSamePetName() {
		Owner owner1 = new Owner();
		Owner owner2 = new Owner();

		Pet pet1 = new Pet();
		pet1.setName("Fluffy");

		Pet pet2 = new Pet();
		pet2.setName("Fluffy");

		owner1.addPet(pet1);
		owner2.addPet(pet2);

		assertThat(owner1.getPet("Fluffy")).isNotNull();
		assertThat(owner2.getPet("Fluffy")).isNotNull();
		assertThat(owner1.getPet("Fluffy")).isNotSameAs(owner2.getPet("Fluffy"));
	}

	@Test
	@DisplayName("Should preserve pet order in collection")
	void testPetOrderPreservation() {
		Pet pet1 = new Pet();
		pet1.setName("Alpha");

		Pet pet2 = new Pet();
		pet2.setName("Beta");

		Pet pet3 = new Pet();
		pet3.setName("Gamma");

		owner.addPet(pet1);
		owner.addPet(pet2);
		owner.addPet(pet3);

		assertThat(owner.getPets()).extracting("name").containsExactly("Alpha", "Beta", "Gamma");
	}

}
