package fr.diptrack.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.diptrack.model.Class;
import fr.diptrack.repository.ClassRepository;
import fr.diptrack.web.dtos.ClassDto;

@Service
public class ClassService {

	@Autowired
	private ClassRepository classRepository;

	public Class saveClass(Class classToSave) {
		return classRepository.save(classToSave);
	}

	public List<Class> findAllClasses() {
		return classRepository.findAll();
	}
	
	public Class findClassById(Long id) {
		return classRepository.findOne(id);
	}

	public void deleteClassById(Long classID) {
		classRepository.delete(classID);
	}

	public void updateClassWithClassDto(ClassDto classDto) {
		Class classFromDB = classRepository.findOne(classDto.getId());
		classFromDB.setName(classDto.getName());
		classFromDB.setFormation(classDto.getFormation());
		classFromDB.setLevel(classDto.getLevel());
		//classe.getListSubjects(classDto.getSubjectList());
		classRepository.save(classFromDB);
	}

}
