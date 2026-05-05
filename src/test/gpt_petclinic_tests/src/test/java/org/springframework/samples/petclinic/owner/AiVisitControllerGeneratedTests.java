package org.springframework.samples.petclinic.owner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VisitController.class)
@DisabledInNativeImage
@DisabledInAotMode
class AiVisitControllerGeneratedTests {

	private static final int OWNER_ID = 1;

	private static final int PET_ID = 7;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	private Owner owner;

	private Pet pet;

	@BeforeEach
	void setUp() {
		PetType dog = new PetType();
		dog.setName("dog");
		this.pet = new Pet();
		this.pet.setName("Leo");
		this.pet.setBirthDate(LocalDate.of(2020, 1, 15));
		this.pet.setType(dog);

		this.owner = new Owner();
		this.owner.setId(OWNER_ID);
		this.owner.setFirstName("George");
		this.owner.setLastName("Franklin");
		this.owner.addPet(this.pet);
		this.pet.setId(PET_ID);

		given(this.owners.findById(OWNER_ID)).willReturn(Optional.of(this.owner));
	}

	@Test
	void loadPetWithVisitPopulatesOwnerPetAndVisit() {
		VisitController controller = new VisitController(this.owners);
		HashMap<String, Object> model = new HashMap<>();

		Visit visit = controller.loadPetWithVisit(OWNER_ID, PET_ID, model);

		assertThat(model).containsEntry("owner", this.owner);
		assertThat(model).containsEntry("pet", this.pet);
		assertThat(visit.getDate()).isEqualTo(LocalDate.now());
		assertThat(this.pet.getVisits()).contains(visit);
	}

	@Test
	void initNewVisitFormReturnsVisitForm() throws Exception {
		this.mockMvc.perform(get("/owners/{ownerId}/pets/{petId}/visits/new", OWNER_ID, PET_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("visit", "pet", "owner"))
			.andExpect(view().name("pets/createOrUpdateVisitForm"));
	}

	@Test
	void processNewVisitFormSavesOwnerAndRedirects() throws Exception {
		this.mockMvc.perform(post("/owners/{ownerId}/pets/{petId}/visits/new", OWNER_ID, PET_ID)
			.param("description", "vaccination"))
			.andExpect(status().is3xxRedirection())
			.andExpect(flash().attributeExists("message"));

		then(this.owners).should().save(any(Owner.class));
	}

}
