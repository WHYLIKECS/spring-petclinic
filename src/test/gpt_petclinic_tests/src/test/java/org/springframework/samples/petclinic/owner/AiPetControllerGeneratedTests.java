package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(PetController.class)
@DisabledInNativeImage
@DisabledInAotMode
class AiPetControllerGeneratedTests {

	private static final int OWNER_ID = 1;

	private static final int PET_ID = 7;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	@MockitoBean
	private PetTypeRepository petTypes;

	private Owner owner;

	private PetType dog;

	@BeforeEach
	void setUp() {
		this.dog = new PetType();
		this.dog.setId(1);
		this.dog.setName("dog");

		Pet existingPet = new Pet();
		existingPet.setName("Leo");
		existingPet.setBirthDate(LocalDate.of(2020, 1, 15));
		existingPet.setType(this.dog);

		this.owner = new Owner();
		this.owner.setId(OWNER_ID);
		this.owner.setFirstName("George");
		this.owner.setLastName("Franklin");
		this.owner.addPet(existingPet);
		existingPet.setId(PET_ID);

		given(this.owners.findById(OWNER_ID)).willReturn(Optional.of(this.owner));
		given(this.petTypes.findPetTypes()).willReturn(List.of(this.dog));
	}

	@Test
	void initCreationFormLoadsOwnerAndPetTypes() throws Exception {
		this.mockMvc.perform(get("/owners/{ownerId}/pets/new", OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner", "pet", "types"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void processCreationFormRejectsDuplicatePetName() throws Exception {
		this.mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID).param("name", "Leo")
			.param("birthDate", "2022-03-10")
			.param("type", "dog"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));

		then(this.owners).should(never()).save(any(Owner.class));
	}

	@Test
	void processCreationFormRejectsFutureBirthDate() throws Exception {
		LocalDate tomorrow = LocalDate.now().plusDays(1);

		this.mockMvc.perform(post("/owners/{ownerId}/pets/new", OWNER_ID).param("name", "Milo")
			.param("birthDate", tomorrow.toString())
			.param("type", "dog"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("pet", "birthDate", "typeMismatch.birthDate"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

	@Test
	void processUpdateFormRejectsRenamingPetToExistingName() throws Exception {
		Pet anotherPet = new Pet();
		anotherPet.setName("Bella");
		anotherPet.setBirthDate(LocalDate.of(2021, 5, 5));
		anotherPet.setType(this.dog);
		this.owner.addPet(anotherPet);
		anotherPet.setId(9);

		this.mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/edit", OWNER_ID, PET_ID).param("id", "7")
			.param("name", "Bella")
			.param("birthDate", "2020-01-15")
			.param("type", "dog"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrorCode("pet", "name", "duplicate"))
			.andExpect(view().name("pets/createOrUpdatePetForm"));
	}

}
