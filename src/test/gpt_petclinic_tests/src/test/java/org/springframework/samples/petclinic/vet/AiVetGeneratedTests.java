package org.springframework.samples.petclinic.vet;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class AiVetGeneratedTests {

	@Test
	void vetSpecialtiesAreReturnedInNameOrderWithoutDuplicates() {
		Vet vet = new Vet();
		Specialty surgery = specialty(2, "surgery");
		Specialty radiology = specialty(1, "radiology");

		vet.addSpecialty(surgery);
		vet.addSpecialty(radiology);
		vet.addSpecialty(surgery);

		assertThat(vet.getNrOfSpecialties()).isEqualTo(2);
		assertThat(vet.getSpecialties()).extracting(Specialty::getName).containsExactly("radiology", "surgery");
	}

	@Test
	void vetsWrapperReturnsMutableList() {
		Vets vets = new Vets();
		vets.getVetList().add(new Vet());

		assertThat(vets.getVetList()).hasSize(1);
	}

	private static Specialty specialty(int id, String name) {
		Specialty specialty = new Specialty();
		specialty.setId(id);
		specialty.setName(name);
		return specialty;
	}

}
