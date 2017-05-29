package com.restdoc.hello;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StringUtils;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.restdocs.snippet.Attributes.key;

/**
 * @author sumit
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GreetingControllerTest {

	@Rule
	public RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

	@Autowired
	private WebApplicationContext context;

	private MockMvc mockMvc;

	private RestDocumentationResultHandler document;

	/**
	 * 
	 */
	@Before
	public void setUp() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
				.apply(documentationConfiguration(this.restDocumentation)).build();
	}

	/**
	 * @throws Exception
	 */
	@Test
	public void greetingGetWithProvidedContent() throws Exception {

		this.mockMvc.perform(get("/greeting").param("name", "Everybody")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.content", is("Hello, Everybody!")))
				.andDo(document("{class-name}/{method-name}",
						requestParameters(parameterWithName("name").description("Greeting's target")),
						responseFields(fieldWithPath("id").description("Greeting's generated id"),
								fieldWithPath("content").description("Greeting's content"),
								fieldWithPath("optionalContent").description("Greeting's optional content")
										.type(JsonFieldType.STRING).optional())));

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void greetingGetWithDefaultContent() throws Exception {

		this.mockMvc.perform(get("/greeting")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.content", is("Hello, World!")))
				.andDo(document("{class-name}/{method-name}",
						responseFields(fieldWithPath("id").ignored(), fieldWithPath("content")
								.description("When name is not provided, this field contains the default value"))));

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void greetingGetWithProvidedContentId() throws Exception {

		this.mockMvc.perform(get("/greeting/abc").param("id", "10029288").param("name", "Everybody"))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(10029288))).andExpect(jsonPath("$.content", is("Hello, Everybody!")))
				.andDo(document("{class-name}/{method-name}",
						requestParameters(
								parameterWithName("id").description("Greeting's id target"),
								parameterWithName("name").description("Greeting's name target")),
						responseFields(fieldWithPath("id").description("Greeting's id"),
								fieldWithPath("content").description("Greeting's content"),
								fieldWithPath("optionalContent").description("Greeting's optional content")
										.type(JsonFieldType.STRING).optional())));

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void greetingGetWithDefaultContentId() throws Exception {

		this.mockMvc.perform(get("/greeting/abc")).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is(1002))).andExpect(jsonPath("$.content", is("Hello, World!")))
				.andDo(document("{class-name}/{method-name}",
						responseFields(
								fieldWithPath("id")
										.description("When id is not provided, this field contains the default value"),
								fieldWithPath("content").description(
										"When name is not provided, this field contains the default value"))));

	}

	/**
	 * @throws Exception
	 */
	@Test
	public void greetingPostWithProvidedContentIdAsJson() throws Exception {

		Greeting anObject = new Greeting(Long.parseLong("23213213"), "Excuse Me I Am A JSON Obj");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(anObject);

		System.out.println(requestJson);

		/*
		 * MvcResult result =
		 * this.mockMvc.perform(post("/greeting/abcd").content(requestJson).
		 * contentType(MediaType.APPLICATION_JSON)) .andDo(print())
		 * .andExpect(status().isBadRequest()) .andReturn(); String content =
		 * result.getResponse().getContentAsString();
		 * System.out.println(content); APPLICATION_JSON_UTF8_VALUE
		 */

		ConstrainedFields fields = new ConstrainedFields(Greeting.class);

		/*
		 * this.document.snippets( requestFields(
		 * fields.withPath("id").description("23213213"),
		 * fields.withPath("content").description("Excuse Me I Am A JSON Obj") )
		 * );
		 */

		this.mockMvc.perform(post("/greeting/abcd").content(requestJson).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(23213213)))
				.andExpect(jsonPath("$.content", is("Excuse Me I Am A JSON Obj")))
				.andDo(document("{class-name}/{method-name}",

						requestFields(fields.withPath("id").description("23213213"),
								fields.withPath("content").description("Excuse Me I Am A JSON Obj")),
						responseFields(fieldWithPath("id").description("Greeting's id"),
								fieldWithPath("content").description("Greeting's content"),
								fieldWithPath("optionalContent").description("Greeting's optional content")
										.type(JsonFieldType.STRING).optional())));

	}

	private static class ConstrainedFields {

		private final ConstraintDescriptions constraintDescriptions;

		/**
		 * @param input
		 */
		ConstrainedFields(Class<?> input) {
			this.constraintDescriptions = new ConstraintDescriptions(input);
		}

		/**
		 * @param path
		 * @return
		 */
		private FieldDescriptor withPath(String path) {
			return fieldWithPath(path).attributes(key("constraints").value(StringUtils
					.collectionToDelimitedString(this.constraintDescriptions.descriptionsForProperty(path), ". ")));
		}
	}

}
