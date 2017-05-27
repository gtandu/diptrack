package fr.diptrack.model;

import java.text.ParseException;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import fr.diptrack.web.dtos.AccountDto;

@Entity
public class Student extends UserAccount {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7482134180300000186L;

	@Column(unique = true)
	private String studentNumber;
	private float average;
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	private Grade classe;

	public Student() {
		super();
	}

	public Student(String mail, String password, String lastName, String firstName, String studentNumber) {
		super(mail, password, lastName, firstName);
		this.studentNumber = studentNumber;
	}

	public Student(AccountDto accountDto) throws ParseException {
		super(accountDto);
		this.studentNumber = accountDto.getStudentNumber();
	}

	public String getStudentNumber() {
		return studentNumber;
	}

	public void setStudentNumber(String studentNumber) {
		this.studentNumber = studentNumber;
	}

	public float getAverage() {
		return average;
	}

	public void setAverage(float average) {
		this.average = average;
	}

	public Grade getClasse() {
		return classe;
	}

	public void setClasse(Grade classe) {
		this.classe = classe;
	}

}
