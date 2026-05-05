package org.springframework.samples.petclinic.vet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledInNativeImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(VetController.class)
@DisabledInNativeImage
@DisabledInAotMode
class AiVetControllerGeneratedTests {

	@Autowired
	private MockMvc mockMvc;

	@MockitoBean
	private VetRepository vetRepository;

	@BeforeEach
	void setUp() {
		given(this.vetRepository.findAll()).willReturn(List.of(vet(1, "James", "Carter"), vet(2, "Helen", "Leary")));
		given(this.vetRepository.findAll(any(Pageable.class)))
			.willReturn(new PageImpl<>(List.of(vet(1, "James", "Carter"), vet(2, "Helen", "Leary"))));
	}

	@Test
	void htmlEndpointExposesPaginationMetadata() throws Exception {
		this.mockMvc.perform(get("/vets.html?page=1"))
			.andExpect(status().isOk())
			.andExpect(model().attributeExists("listVets", "currentPage", "totalPages", "totalItems"))
			.andExpect(view().name("vets/vetList"));
	}

	@Test
	void jsonEndpointReturnsVetListPayload() throws Exception {
		this.mockMvc.perform(get("/vets").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
			.andExpect(jsonPath("$.vetList.length()").value(2))
			.andExpect(jsonPath("$.vetList[1].lastName").value("Leary"));
	}

	private static Vet vet(int id, String firstName, String lastName) {
		Vet vet = new Vet();
		vet.setId(id);
		vet.setFirstName(firstName);
		vet.setLastName(lastName);
		return vet;
	}

}
