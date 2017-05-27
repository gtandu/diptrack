package fr.diptrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.diptrack.model.Grade;
import fr.diptrack.repository.GradeRepository;
import fr.diptrack.web.dtos.GradeDto;

@Service
public class GradeService {

	@Autowired
	private GradeRepository gradeRepository;

	public Grade saveGrade(Grade grade) {
		return gradeRepository.save(grade);
	}

	public List<Grade> findAllGrade() {
		return gradeRepository.findAll();
	}

	public void deleteGradeById(long classID) {
		gradeRepository.delete(classID);
	}

	public void updateGradeWithGradeDto(GradeDto gradeDto) {
		Grade grade = gradeRepository.findOne(gradeDto.getId());
		grade.setName(gradeDto.getName());
		grade.setFormation(gradeDto.getFormation());
		grade.setLevel(gradeDto.getLevel());
		gradeRepository.save(grade);
	}

}