package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.OwnerRepository;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.PetTypeRepository;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.samples.petclinic.vet.VetRepository;

@DataJpaTest
class AiClinicServiceGeneratedTests {

	@Autowired
	private OwnerRepository owners;

	@Autowired
	private PetTypeRepository petTypes;

	@Autowired
	private VetRepository vets;

	@Test
	void ownerSearchSupportsPrefixQueriesAndPaging() {
		Page<Owner> page = this.owners.findByLastNameStartingWith("D", PageRequest.of(0, 2));

		assertThat(page.getContent()).isNotEmpty();
		assertThat(page.getSize()).isEqualTo(2);
		assertThat(page.getContent()).allMatch(owner -> owner.getLastName().startsWith("D"));
	}

	@Test
	void petTypesAreReturnedInAlphabeticalOrder() {
		List<PetType> types = this.petTypes.findPetTypes();

		assertThat(types).isNotEmpty();
		assertThat(types).extracting(PetType::getName).isSorted();
	}

	@Test
	void vetsRepositoryReturnsPageConsistentWithFullList() {
		List<Vet> allVets = List.copyOf(this.vets.findAll());
		Page<Vet> firstPage = this.vets.findAll(PageRequest.of(0, 3));

		assertThat(firstPage.getContent()).hasSizeLessThanOrEqualTo(3);
		assertThat(allVets).containsAll(firstPage.getContent());
	}

	@Test
	void entityUtilsThrowsWhenRequestedIdIsMissing() {
		PetType cat = new PetType();
		cat.setId(1);
		cat.setName("cat");

		assertThatThrownBy(() -> EntityUtils.getById(List.of(cat), PetType.class, 999))
			.isInstanceOf(ObjectRetrievalFailureException.class);
	}

}
