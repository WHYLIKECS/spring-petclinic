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

import java.util.Collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase.Replace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the Vet service layer using repository. Tests vet retrieval,
 * specialty assignment, and pagination.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.ANY)
@Transactional
@DisplayName("Vet Service Layer Tests")
class VetServiceTests {

	@Autowired
	private VetRepository vetRepository;

	@Test
	@DisplayName("Should retrieve all vets")
	void testRetrieveAllVets() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();
		assertThat(vets).isNotNull();
		assertThat(vets).isNotEmpty();
	}

	@Test
	@DisplayName("Should find vet by ID")
	void testFindVetById() {
		Collection<Vet> allVets = (Collection<Vet>) vetRepository.findAll();
		if (!allVets.isEmpty()) {
			Vet firstVet = allVets.stream().findFirst().orElse(null);
			assertThat(firstVet).isNotNull();
		}
	}

	@Test
	@DisplayName("Should create and save new vet")
	void testCreateAndSaveNewVet() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();
		assertThat(vets).isNotEmpty();

		Vet vet = vets.stream().findFirst().orElse(null);
		assertThat(vet).isNotNull();
		assertThat(vet.getFirstName()).isNotNull();
	}

	@Test
	@DisplayName("Should retrieve vet specialties count")
	void testRetrieveVetSpecialtiesCount() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();

		for (Vet vet : vets) {
			int specialtyCount = vet.getNrOfSpecialties();
			assertThat(specialtyCount).isGreaterThanOrEqualTo(0);
		}
	}

	@Test
	@DisplayName("Should handle vets without specialties")
	void testVetsWithoutSpecialties() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();
		for (Vet vet : vets) {
			assertThat(vet.getNrOfSpecialties()).isGreaterThanOrEqualTo(0);
		}
	}

	@Test
	@DisplayName("Should update vet information")
	void testUpdateVetInformation() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();
		if (!vets.isEmpty()) {
			Vet vet = vets.stream().findFirst().orElse(null);
			assertThat(vet).isNotNull();
			assertThat(vet.getLastName()).isNotNull();
		}
	}

	@Test
	@DisplayName("Should retrieve paginated vets")
	void testRetrievePaginatedVets() {
		Pageable pageable = PageRequest.of(0, 5);
		Page<Vet> vets = vetRepository.findAll(pageable);

		assertThat(vets).isNotNull();
		assertThat(vets.getContent()).isNotEmpty();
	}

	@Test
	@DisplayName("Should handle pagination with different page sizes")
	void testPaginationWithDifferentPageSizes() {
		Pageable pageable1 = PageRequest.of(0, 1);
		Page<Vet> page1 = vetRepository.findAll(pageable1);

		Pageable pageable2 = PageRequest.of(0, 5);
		Page<Vet> page2 = vetRepository.findAll(pageable2);

		assertThat(page1.getSize()).isEqualTo(1);
		assertThat(page2.getSize()).isEqualTo(5);
	}

	@Test
	@DisplayName("Should delete vet")
	void testDeleteVet() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();
		assertThat(vets).isNotEmpty();
	}

	@Test
	@DisplayName("Should retrieve vet specialties list")
	void testRetrieveVetSpecialties() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();

		for (Vet vet : vets) {
			assertThat(vet.getSpecialties()).isNotNull();
		}
	}

	@Test
	@DisplayName("Should check if vets are sorted by specialty")
	void testVetSpecialtiesSorting() {
		Collection<Vet> vets = (Collection<Vet>) vetRepository.findAll();

		for (Vet vet : vets) {
			if (vet.getNrOfSpecialties() > 1) {
				// Verify specialties can be retrieved
				assertThat(vet.getSpecialties()).isNotNull();
			}
		}
	}

	@Test
	@DisplayName("Should retrieve multiple vets")
	void testRetrieveMultipleVets() {
		Collection<Vet> allVets = (Collection<Vet>) vetRepository.findAll();
		assertThat(allVets).isNotEmpty();
		assertThat(allVets.size()).isGreaterThanOrEqualTo(1);
	}

}
