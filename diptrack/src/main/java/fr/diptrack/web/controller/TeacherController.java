package fr.diptrack.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import fr.diptrack.model.Mark;
import fr.diptrack.model.Student;
import fr.diptrack.model.Subject;
import fr.diptrack.model.Teacher;
import fr.diptrack.model.UserAccount;
import fr.diptrack.model.enumeration.MarkTypeEnum;
import fr.diptrack.service.AccountService;
import fr.diptrack.service.MarkService;
import fr.diptrack.service.StudentService;
import fr.diptrack.service.SubjectService;
import fr.diptrack.service.TeacherService;
import fr.diptrack.service.mail.PublishMarkMail;
import fr.diptrack.web.dtos.MarkDto;
import fr.diptrack.web.dtos.MarkManagementForm;

@Controller
@RequestMapping(value = "/enseignant")
public class TeacherController {

	public static final String URL_HOME_PAGE_TEACHER = "/homePageTeacher.html";
	public static final String URL_NOTE_GRADE_MANAGEMENT = "/noteGradeManagement.html";

	@Autowired
	public AccountService accountService;
	
	@Autowired
	public TeacherService teacherService;
	
	@Autowired
	public StudentService studentService;
	
	@Autowired
	public SubjectService subjectService;
	
	@Autowired
	public PublishMarkMail publishMarkMail;
	
	@Autowired
	public MarkService markService;
	
	@RequestMapping(value = URL_HOME_PAGE_TEACHER, method = RequestMethod.GET)
	public ModelAndView showPageHomePageteacher() {
		ModelAndView model = new ModelAndView("homePageTeacher");

		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserAccount account = accountService.findByMail(auth.getName());

		model.addObject("account", account);

		return model;
	}

	@RequestMapping(value = URL_NOTE_GRADE_MANAGEMENT, method = RequestMethod.GET)
	public ModelAndView showPageNoteGradeManagement() {
		ModelAndView model = new ModelAndView("noteGradeManagement");
		
		MarkManagementForm markManagementForm = new MarkManagementForm();
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Teacher teacher = teacherService.findByMail(auth.getName());
		String subjectName = teacher.getSubject();
		
		List<Student> listStudents = studentService.findAllStudentBySubject(subjectName);
		List<MarkDto> listMarkDto = new ArrayList<>();
		
		for (Student student : listStudents) {
			MarkDto mark = new MarkDto(student.getMail(), student.getFirstName(), student.getLastName(), student.getListMarks().get(0).getMark(), student.getListMarks().get(1).getMark(), student.getListSubjects().get(0).getId(), student.getListMarks().get(0).getId(), student.getListMarks().get(1).getId());
			listMarkDto.add(mark);
		}
		
		markManagementForm.setListMarkDto(listMarkDto);
		
		model.addObject("markManagementForm", markManagementForm);
		model.addObject("subjectName",subjectName);
		
		return model;
		
	}
	
	@RequestMapping(value = URL_NOTE_GRADE_MANAGEMENT, method = RequestMethod.POST, params = { "save" })
	public String saveMarks(@ModelAttribute(value = "markManagementForm") MarkManagementForm markManagementForm,BindingResult result) {
		
		List<MarkDto> listMarkDto = markManagementForm.getListMarkDto();
		
		for (MarkDto markDto : listMarkDto) {
			Student student = (Student) accountService.findByMail(markDto.getMail());
			Subject subject = subjectService.findById(markDto.getIdSubject());
			Mark markCc = null;
			Mark markExam = null;
			List<Mark> studentListMark = student.getListMarks();
			if(markDto.getIdMarkCc() != null)
			{
				markService.updateMark(markDto.getIdMarkCc(), markDto.getMarkCc());
			}
			else
			{
				markCc = new Mark(markDto.getMarkCc(),student,subject,MarkTypeEnum.Continu);
				studentListMark.add(markCc);
			}
			
			if(markDto.getIdMarkExam() != null){
				
				markService.updateMark(markDto.getIdMarkExam(), markDto.getMarkExam());
			}
			else{
				markExam = new Mark(markDto.getMarkExam(),student,subject,MarkTypeEnum.Partiel);
				studentListMark.add(markExam);
			}
			
			student.setListMarks(studentListMark);
			studentService.updateStudent(student);
			
		}
		return "redirect:/enseignant"+URL_NOTE_GRADE_MANAGEMENT;
	}	
	
	@RequestMapping(value = URL_NOTE_GRADE_MANAGEMENT, method = RequestMethod.POST, params = { "publish" })
	public String publishMark(@ModelAttribute(value = "markManagementForm") MarkManagementForm markManagementForm,BindingResult result) {
		
		List<MarkDto> listMarkDto = markManagementForm.getListMarkDto();
		
		for (MarkDto markDto : listMarkDto) {
			Student student = (Student) accountService.findByMail(markDto.getMail());
			Subject subject = subjectService.findById(markDto.getIdSubject());
			Mark markCc = null;
			Mark markExam = null;
			List<Mark> studentListMark = student.getListMarks();
			if(markDto.getIdMarkCc() != null)
			{
				markService.updateMark(markDto.getIdMarkCc(), markDto.getMarkCc());
			}
			else
			{
				markCc = new Mark(markDto.getMarkCc(),student,subject,MarkTypeEnum.Continu);
				studentListMark.add(markCc);
			}
			
			if(markDto.getIdMarkExam() != null){
				
				markService.updateMark(markDto.getIdMarkExam(), markDto.getMarkExam());
			}
			else{
				markExam = new Mark(markDto.getMarkExam(),student,subject,MarkTypeEnum.Partiel);
				studentListMark.add(markExam);
			}
			
			student.setListMarks(studentListMark);
			studentService.updateStudent(student);
			
			publishMarkMail.sendMailPublishMarks(markDto.getMail(), subject.getName(), markCc.getMark(), markExam.getMark());
		}
		
		
		return "redirect:/enseignant"+URL_NOTE_GRADE_MANAGEMENT;
	}
}