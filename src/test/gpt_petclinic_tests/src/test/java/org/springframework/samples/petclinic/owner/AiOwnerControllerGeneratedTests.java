package org.springframework.samples.petclinic.owner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(OwnerController.class)
@DisabledInNativeImage
@DisabledInAotMode
class AiOwnerControllerGeneratedTests {

	private static final int OWNER_ID = 1;

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private OwnerRepository owners;

	private Owner owner;

	@BeforeEach
	void setUp() {
		this.owner = new Owner();
		this.owner.setId(OWNER_ID);
		this.owner.setFirstName("George");
		this.owner.setLastName("Franklin");
		this.owner.setAddress("110 W. Liberty St.");
		this.owner.setCity("Madison");
		this.owner.setTelephone("6085551023");
		given(this.owners.findById(OWNER_ID)).willReturn(Optional.of(this.owner));
	}

	@Test
	void processFindFormWithoutLastNameLoadsOwnersList() throws Exception {
		Owner secondOwner = new Owner();
		secondOwner.setId(2);
		secondOwner.setFirstName("Betty");
		secondOwner.setLastName("Davis");
		given(this.owners.findByLastNameStartingWith(eq(""), any(Pageable.class)))
			.willReturn(new PageImpl<>(List.of(this.owner, secondOwner)));

		this.mockMvc.perform(get("/owners"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("listOwners"))
			.andExpect(model().attribute("currentPage", 1))
			.andExpect(view().name("owners/ownersList"));
	}

	@Test
	void processCreationFormWithValidationErrorsDoesNotSaveOwner() throws Exception {
		this.mockMvc.perform(post("/owners/new").param("firstName", "Amy").param("lastName", "Pond"))
			.andExpect(status().isOk())
			.andExpect(model().attributeHasFieldErrors("owner", "address", "city", "telephone"))
			.andExpect(view().name("owners/createOrUpdateOwnerForm"));

		then(this.owners).should(never()).save(any(Owner.class));
	}

	@Test
	void processUpdateOwnerFormAddsMismatchFlashError() throws Exception {
		this.mockMvc.perform(post("/owners/{ownerId}/edit", OWNER_ID).param("id", "2")
			.param("firstName", "George")
			.param("lastName", "Franklin")
			.param("address", "110 W. Liberty St.")
			.param("city", "Madison")
			.param("telephone", "6085551023"))
			.andExpect(status().is3xxRedirection())
			.andExpect(redirectedUrl("/owners/1/edit"))
			.andExpect(flash().attributeExists("error"));
	}

	@Test
	void showOwnerReturnsOwnerDetailsView() throws Exception {
		this.mockMvc.perform(get("/owners/{ownerId}", OWNER_ID))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("owner"))
			.andExpect(view().name("owners/ownerDetails"));
	}

}
