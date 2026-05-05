package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AiOwnerDomainGeneratedTests {

	private Owner owner;

	private Pet persistedPet;

	@BeforeEach
	void setUp() {
		this.owner = new Owner();
		this.owner.setFirstName("George");
		this.owner.setLastName("Franklin");
		this.owner.setAddress("110 W. Liberty St.");
		this.owner.setCity("Madison");
		this.owner.setTelephone("6085551023");

		this.persistedPet = createPet("Max");
		this.owner.addPet(this.persistedPet);
		this.persistedPet.setId(7);
	}

	@Test
	void addPetSkipsPersistedPetInstances() {
		Pet persisted = createPet("Buddy");
		persisted.setId(9);

		this.owner.addPet(persisted);

		assertThat(this.owner.getPets()).containsExactly(this.persistedPet);
	}

	@Test
	void getPetCanIgnoreUnsavedDuplicates() {
		Pet unsavedDuplicate = createPet("MAX");
		this.owner.addPet(unsavedDuplicate);

		assertThat(this.owner.getPet("max", true)).isSameAs(this.persistedPet);
		assertThat(this.owner.getPet("max", false)).isSameAs(this.persistedPet);
	}

	@Test
	void addVisitRejectsNullArgumentsAndAssociatesVisitWithExistingPet() {
		Visit visit = new Visit();
		visit.setDescription("annual checkup");

		assertThatThrownBy(() -> this.owner.addVisit(null, visit)).isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Pet identifier must not be null");
		assertThatThrownBy(() -> this.owner.addVisit(this.persistedPet.getId(), null))
			.isInstanceOf(IllegalArgumentException.class)
			.hasMessageContaining("Visit must not be null");

		int before = this.persistedPet.getVisits().size();
		this.owner.addVisit(this.persistedPet.getId(), visit);

		assertThat(this.persistedPet.getVisits()).hasSize(before + 1);
		assertThat(this.persistedPet.getVisits()).contains(visit);
	}

	@Test
	void toStringIncludesCoreOwnerFields() {
		String text = this.owner.toString();

		assertThat(text).contains("George");
		assertThat(text).contains("Franklin");
		assertThat(text).contains("Madison");
		assertThat(text).contains("6085551023");
	}

	private Pet createPet(String name) {
		Pet pet = new Pet();
		pet.setName(name);
		pet.setBirthDate(LocalDate.of(2020, 2, 20));
		PetType type = new PetType();
		type.setName("dog");
		pet.setType(type);
		return pet;
	}

}
