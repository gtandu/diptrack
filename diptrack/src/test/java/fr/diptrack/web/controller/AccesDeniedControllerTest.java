package fr.diptrack.web.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import fr.diptrack.web.controller.AccesDeniedController;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class AccesDeniedControllerTest {

	@InjectMocks
	private AccesDeniedController accesRefuseController;

	@Autowired
	private WebApplicationContext wac;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).alwaysDo(MockMvcResultHandlers.print())
				.apply(springSecurity()).build();
	}

	@Test
	@WithMockUser(authorities = "Administrator")
	public void shouldShowPageError403() throws Exception {

		this.mockMvc.perform(get(AccesDeniedController.URL_ACCESS_DENIED).accept(MediaType.TEXT_HTML))
				.andExpect(status().isOk()).andExpect(view().name("error/403"));
	}

}
