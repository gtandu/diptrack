package fr.diptrack.web.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.fasterxml.jackson.databind.ObjectMapper;

import fr.diptrack.exception.CourseExistException;
import fr.diptrack.model.Course;
import fr.diptrack.model.Semester;
import fr.diptrack.model.Subject;
import fr.diptrack.model.Ue;
import fr.diptrack.service.ClassService;
import fr.diptrack.service.CourseService;
import fr.diptrack.service.SemesterService;
import fr.diptrack.service.SubjectService;
import fr.diptrack.service.UeService;
import fr.diptrack.web.dtos.CourseDto;
import fr.diptrack.web.dtos.SemesterDto;
import fr.diptrack.web.dtos.SemesterIdSubjectNameDto;
import fr.diptrack.web.dtos.SubjectDto;
import fr.diptrack.web.dtos.SubjectIdUeCoefficientDto;
import fr.diptrack.web.dtos.SubjectNameUeIdDto;
import fr.diptrack.web.dtos.UeDto;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class AjaxResponsibleControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private AjaxResponsibleController ajaxResponsibleController;

	@Mock
	private CourseService courseService;

	@Mock
	private SemesterService semesterService;

	@Mock
	private SubjectService subjectService;

	@Mock
	private UeService ueService;

	@Mock
	private ClassService classService;

	private final String URL_RESPONSIBLE_CONTROLLER = "/responsable";

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(ajaxResponsibleController).setViewResolvers(viewResolver())
				.alwaysDo(MockMvcResultHandlers.print()).build();
	}

	@Test
	public void testCreateCourse() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		CourseDto courseDto = new CourseDto("Test", "", "");
		Course course = mock(Course.class);
		course.setId(new Long("4"));

		when(courseService.saveCourseFromDto(any(CourseDto.class))).thenReturn(course);

		// GIVEN
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CREATE_COURSE)
						.param("courseDto", mapper.writeValueAsString(courseDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// THEN

		verify(courseService).saveCourseFromDto(any(CourseDto.class));
		verify(course).getId();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCreateCourseThrowBranchExistException() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		CourseDto courseDto = new CourseDto("Test", "", "");
		Course course = mock(Course.class);
		course.setId(new Long("4"));

		when(courseService.saveCourseFromDto(any(CourseDto.class))).thenThrow(CourseExistException.class);

		// GIVEN
		final MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CREATE_COURSE)
						.param("courseDto", mapper.writeValueAsString(courseDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		// THEN
		assertThat(result.getResponse().getContentAsString()).isEqualTo("0");
		verify(courseService).saveCourseFromDto(any(CourseDto.class));

	}

	@Test
	public void testEditCourse() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		CourseDto courseDto = new CourseDto("Test", "", "");
		Course course = mock(Course.class);
		course.setId(new Long("4"));

		when(courseService.editCourseName(any(CourseDto.class))).thenReturn(course);

		// GIVEN
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_EDIT_COURSE_NAME)
						.param("courseDto", mapper.writeValueAsString(courseDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());

		// THEN

		verify(courseService).editCourseName(any(CourseDto.class));
		verify(course).getId();

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testEditeCourseThrowBranchExistException() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		CourseDto courseDto = new CourseDto("Test", "", "");
		Course course = mock(Course.class);
		course.setId(new Long("4"));

		when(courseService.editCourseName(any(CourseDto.class))).thenThrow(CourseExistException.class);

		// GIVEN
		final MvcResult result = this.mockMvc
				.perform(MockMvcRequestBuilders
						.post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_EDIT_COURSE_NAME)
						.param("courseDto", mapper.writeValueAsString(courseDto))
						.contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();

		// THEN
		assertThat(result.getResponse().getContentAsString()).isEqualTo("0");
		verify(courseService).editCourseName(any(CourseDto.class));

	}

	@Test
	public void testCreateSemester() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SemesterDto semesterDto = new SemesterDto();
		Semester semester = mock(Semester.class);
		when(semesterService.saveSemesterFromDto(any(SemesterDto.class))).thenReturn(semester);

		// GIVEN
		this.mockMvc.perform(
				MockMvcRequestBuilders.post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CREATE_SEMESTER)
						.param("semesterDTO", mapper.writeValueAsString(semesterDto))
						.contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());

		// THEN
		verify(semesterService).saveSemesterFromDto(any(SemesterDto.class));
		verify(semester).getId();
	}

	@Test
	public void testDeleteSemester() throws Exception {
		// WHEN

		doNothing().when(semesterService).deleteSemesterById(anyLong());

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_DELETE_SEMESTER)
				.param("id", "3").contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());

		// THEN
		verify(semesterService).deleteSemesterById(anyLong());
	}

	@Test
	public void testUpdateNumberSemester() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SemesterDto semesterDto = new SemesterDto();
		doNothing().when(semesterService).updateNumberSemester(any(SemesterDto.class));

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_UPDATE_NUMBER_SEMESTER)
				.param("semesterDto", mapper.writeValueAsString(semesterDto))).andExpect(status().isOk());

		// THEN
		verify(semesterService).updateNumberSemester(any(SemesterDto.class));
	}

	@Test
	public void testCreateUe() throws Exception {
		// WHEN
		UeDto ueDto = new UeDto();
		ObjectMapper mapper = new ObjectMapper();
		Ue ue = new Ue();
		ue.setId(new Long(3));
		when(ueService.createUeFromUeDto(any(UeDto.class))).thenReturn(ue);

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CREATE_UE).param("ueDto",
				mapper.writeValueAsString(ueDto))).andExpect(status().isOk());

		// THEN
		verify(ueService).createUeFromUeDto(any(UeDto.class));

	}

	@Test
	public void testDeleteUe() throws Exception {
		// WHEN

		doNothing().when(ueService).deleteUeById(anyLong());

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_DELETE_UE).param("id", "3")
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk());

		// THEN
		verify(ueService).deleteUeById(anyLong());
	}

	@Test
	public void testUpdateNumberUe() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		UeDto ueDto = new UeDto();
		doNothing().when(ueService).updateNumberUe(any(UeDto.class));

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_UPDATE_NUMBER_UE)
				.param("ueDto", mapper.writeValueAsString(ueDto))).andExpect(status().isOk());

		// THEN
		verify(ueService).updateNumberUe(any(UeDto.class));
	}

	private ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/templates/");
		viewResolver.setSuffix(".html");

		return viewResolver;
	}

	@Test
	public void testCreateSubject() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SubjectDto subjectDto = new SubjectDto();
		Subject subject = new Subject();
		subject.setId(new Long(2));
		SubjectIdUeCoefficientDto subjectIdUeCoefficientDto = new SubjectIdUeCoefficientDto();
		when(subjectService.saveSubjectFromDto(any(SubjectDto.class))).thenReturn(subjectIdUeCoefficientDto);

		// GIVEN

		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CREATE_SUBJECT)
				.accept(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(subjectDto))
				.contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

		// THEN
		verify(subjectService).saveSubjectFromDto(any(SubjectDto.class));
	}

	@Test
	public void testDeleteSubject() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SubjectNameUeIdDto subjectNameUeIdDto = new SubjectNameUeIdDto();
		when(subjectService.deleteSubjectByName(any(SubjectNameUeIdDto.class))).thenReturn(new Integer(2));

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_DELETE_SUBJECT)
				.param("dto", mapper.writeValueAsString(subjectNameUeIdDto))).andExpect(status().isOk());

		// THEN

		verify(subjectService).deleteSubjectByName(any(SubjectNameUeIdDto.class));

	}

	@Test
	public void testUpdateStudentInfo() throws Exception {

		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SubjectDto subjectDto = new SubjectDto();
		doNothing().when(subjectService).updateSubjectFromDto((any(SubjectDto.class)));

		// GIVEN
		this.mockMvc.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_UPDATE_SUBJECT)
				.param("subjectDto", mapper.writeValueAsString(subjectDto))).andExpect(status().isOk());

		// THEN

		verify(subjectService).updateSubjectFromDto((any(SubjectDto.class)));

	}

	@Test
	public void testCheckSubjectNameExistInBranch() throws Exception {
		// WHEN
		ObjectMapper mapper = new ObjectMapper();
		SemesterIdSubjectNameDto semesterIdSubjectNameDto = new SemesterIdSubjectNameDto();
		when(subjectService.checkSubjectExistInBranch(any(SemesterIdSubjectNameDto.class))).thenReturn(true);

		// GIVEN
		this.mockMvc
				.perform(post(URL_RESPONSIBLE_CONTROLLER + AjaxResponsibleController.URL_CHECK_SUBJECT_NAME_IN_COURSE)
						.param("dto", mapper.writeValueAsString(semesterIdSubjectNameDto)))
				.andExpect(status().isOk());

		// THEN

		verify(subjectService).checkSubjectExistInBranch((any(SemesterIdSubjectNameDto.class)));
	}

}