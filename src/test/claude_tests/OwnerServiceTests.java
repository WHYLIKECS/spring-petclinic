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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the Owner service layer using repositories. Tests owner
 * operations, pet management, and visit handling.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
@DisplayName("Owner Service Layer Tests")
class OwnerServiceTests {

	@Autowired
	private OwnerRepository ownerRepository;

	@Autowired
	private PetTypeRepository petTypeRepository;

	@Test
	@DisplayName("Should find all owners")
	void testFindAllOwners() {
		Collection<Owner> owners = (Collection<Owner>) ownerRepository.findAll();
		assertThat(owners).isNotNull();
	}

	@Test
	@DisplayName("Should create and save new owner")
	void testCreateAndSaveOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Jane");
		owner.setLastName("Smith");
		owner.setAddress("456 Oak Ave");
		owner.setCity("Shelbyville");
		owner.setTelephone("5555552020");

		Owner savedOwner = ownerRepository.save(owner);

		assertThat(savedOwner.getId()).isNotNull();
		assertThat(savedOwner.getFirstName()).isEqualTo("Jane");
	}

	@Test
	@DisplayName("Should find owner by ID")
	void testFindOwnerById() {
		Owner owner = new Owner();
		owner.setFirstName("Robert");
		owner.setLastName("Brown");
		owner.setAddress("789 Pine Rd");
		owner.setCity("Capital City");
		owner.setTelephone("5555553030");

		Owner savedOwner = ownerRepository.save(owner);
		Optional<Owner> foundOwner = ownerRepository.findById(savedOwner.getId());

		assertThat(foundOwner).isPresent();
		assertThat(foundOwner.get().getFirstName()).isEqualTo("Robert");
	}

	@Test
	@DisplayName("Should search owners by last name")
	void testSearchOwnersByLastName() {
		Owner owner = new Owner();
		owner.setFirstName("Michael");
		owner.setLastName("Johnson");
		owner.setAddress("321 Main St");
		owner.setCity("Springfield");
		owner.setTelephone("5555554040");

		ownerRepository.save(owner);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> results = ownerRepository.findByLastNameStartingWith("John", pageable);

		assertThat(results).isNotNull();
		assertThat(results.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("Should update owner information")
	void testUpdateOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Sarah");
		owner.setLastName("Davis");
		owner.setAddress("100 Elm St");
		owner.setCity("Lanchester");
		owner.setTelephone("5555555050");

		Owner savedOwner = ownerRepository.save(owner);
		savedOwner.setTelephone("5555555051");
		Owner updatedOwner = ownerRepository.save(savedOwner);

		Optional<Owner> retrieved = ownerRepository.findById(updatedOwner.getId());
		assertThat(retrieved.get().getTelephone()).isEqualTo("5555555051");
	}

	@Test
	@DisplayName("Should add pet to owner")
	void testAddPetToOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Emma");
		owner.setLastName("Wilson");
		owner.setAddress("555 Maple Dr");
		owner.setCity("Shelbyville");
		owner.setTelephone("5555556060");

		Owner savedOwner = ownerRepository.save(owner);

		Pet pet = new Pet();
		pet.setName("Max");
		pet.setBirthDate(LocalDate.of(2021, 3, 10));
		Collection<PetType> petTypes = (Collection<PetType>) petTypeRepository.findAll();
		if (!petTypes.isEmpty()) {
			pet.setType(petTypes.stream().findFirst().orElse(null));
		}
		savedOwner.addPet(pet);

		Owner updatedOwner = ownerRepository.save(savedOwner);
		assertThat(updatedOwner.getPets()).hasSize(1);
		assertThat(updatedOwner.getPet("Max")).isNotNull();
	}

	@Test
	@DisplayName("Should add multiple pets to owner")
	void testAddMultiplePetsToOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Lucas");
		owner.setLastName("Martin");
		owner.setAddress("789 Cedar Ln");
		owner.setCity("Capital City");
		owner.setTelephone("5555557070");

		Owner savedOwner = ownerRepository.save(owner);

		Collection<PetType> petTypes = (Collection<PetType>) petTypeRepository.findAll();
		PetType defaultType = petTypes.stream().findFirst().orElse(null);

		Pet pet1 = new Pet();
		pet1.setName("Buddy");
		pet1.setBirthDate(LocalDate.of(2020, 5, 15));
		pet1.setType(defaultType);

		Pet pet2 = new Pet();
		pet2.setName("Luna");
		pet2.setBirthDate(LocalDate.of(2022, 8, 20));
		pet2.setType(defaultType);

		savedOwner.addPet(pet1);
		savedOwner.addPet(pet2);

		Owner updatedOwner = ownerRepository.save(savedOwner);
		assertThat(updatedOwner.getPets()).hasSize(2);
	}

	@Test
	@DisplayName("Should delete owner")
	void testDeleteOwner() {
		Owner owner = new Owner();
		owner.setFirstName("David");
		owner.setLastName("Taylor");
		owner.setAddress("900 Birch Ave");
		owner.setCity("Lanchester");
		owner.setTelephone("5555558080");

		Owner savedOwner = ownerRepository.save(owner);
		Integer ownerId = savedOwner.getId();

		ownerRepository.delete(savedOwner);
		Optional<Owner> deletedOwner = ownerRepository.findById(ownerId);

		assertThat(deletedOwner).isEmpty();
	}

	@Test
	@DisplayName("Should set pet type on pet")
	void testSetPetTypeOnPet() {
		Owner owner = new Owner();
		owner.setFirstName("Anna");
		owner.setLastName("Lee");
		owner.setAddress("246 Spruce St");
		owner.setCity("Capital City");
		owner.setTelephone("5555559090");

		Owner savedOwner = ownerRepository.save(owner);

		Pet pet = new Pet();
		pet.setName("Whiskers");
		pet.setBirthDate(LocalDate.of(2021, 12, 5));
		Collection<PetType> petTypes = (Collection<PetType>) petTypeRepository.findAll();
		if (!petTypes.isEmpty()) {
			pet.setType(petTypes.stream().findFirst().orElse(null));
		}
		savedOwner.addPet(pet);

		Owner updatedOwner = ownerRepository.save(savedOwner);
		assertThat(savedOwner.getPets()).hasSize(1);
	}

	@Test
	@DisplayName("Should add visit to pet through owner")
	void testAddVisitToPetThroughOwner() {
		Owner owner = new Owner();
		owner.setFirstName("Chris");
		owner.setLastName("Anderson");
		owner.setAddress("135 Oak St");
		owner.setCity("Shelbyville");
		owner.setTelephone("5555550101");

		Owner savedOwner = ownerRepository.save(owner);

		Pet pet = new Pet();
		pet.setName("Fluffy");
		pet.setBirthDate(LocalDate.of(2020, 7, 12));
		Collection<PetType> petTypes = (Collection<PetType>) petTypeRepository.findAll();
		if (!petTypes.isEmpty()) {
			pet.setType(petTypes.stream().findFirst().orElse(null));
		}
		savedOwner.addPet(pet);

		Owner updatedOwner = ownerRepository.save(savedOwner);

		Visit visit = new Visit();
		visit.setDate(LocalDate.now());
		visit.setDescription("Routine checkup");

		Pet foundPet = updatedOwner.getPet("Fluffy");
		foundPet.addVisit(visit);

		Owner finalOwner = ownerRepository.save(updatedOwner);
		Pet petWithVisit = finalOwner.getPet("Fluffy");

		assertThat(petWithVisit.getVisits()).hasSize(1);
	}

	@Test
	@DisplayName("Should retrieve all pet types")
	void testRetrieveAllPetTypes() {
		Collection<PetType> petTypes = (Collection<PetType>) petTypeRepository.findAll();
		assertThat(petTypes).isNotNull();
		assertThat(petTypes).isNotEmpty();
	}

	@Test
	@DisplayName("Should paginate owner search results")
	void testPaginateOwnerSearchResults() {
		Owner owner1 = new Owner();
		owner1.setFirstName("Bob");
		owner1.setLastName("Newman");
		owner1.setAddress("555 City St");
		owner1.setCity("Springfield");
		owner1.setTelephone("5555551111");
		ownerRepository.save(owner1);

		Pageable pageable = PageRequest.of(0, 10);
		Page<Owner> results = ownerRepository.findByLastNameStartingWith("N", pageable);

		assertThat(results.getContent()).isNotNull();
	}

}
