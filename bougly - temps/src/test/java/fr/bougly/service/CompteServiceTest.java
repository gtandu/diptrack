package fr.bougly.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import fr.bougly.builder.model.AdministrateurBuilder;
import fr.bougly.builder.model.EtudiantBuilder;
import fr.bougly.exception.UserExistException;
import fr.bougly.model.Administrateur;
import fr.bougly.model.CompteUtilisateur;
import fr.bougly.model.Etudiant;
import fr.bougly.model.enumeration.RoleCompteEnum;
import fr.bougly.model.security.Authority;
import fr.bougly.repository.CompteRepository;
import fr.bougly.repository.security.AuthorityRepository;
import fr.bougly.service.mail.ServiceMail;
import fr.bougly.web.beans.CompteBean;

@RunWith(MockitoJUnitRunner.class)
public class CompteServiceTest {
	
	@InjectMocks
	private CompteService compteService;
	
	@Mock
	private ServiceMail serviceMail;
	
	@Mock
	private CompteRepository<CompteUtilisateur> compteRepository;
	
	@Mock
	private AuthorityRepository authorityRepository;

	
	@Test
	public void shouldCallCheckUserMailAndSaveUser() throws Exception {
		//WHEN
		String mail = "test@test.fr";
		String mdp = "test";
		String nom = "Dalton";
		String prenom = "Joe";
		String dateDeNaissance ="01/01/2000";
		String role = "ADMIN";
		Administrateur administrateur = new AdministrateurBuilder().avecMail(mail).avecMdp(mdp).avecNom(nom).avecPrenom(prenom).avecDateDeNaissance(dateDeNaissance).build();
		when(compteRepository.findByMail(anyString())).thenReturn(null);
		when(compteRepository.save(any(CompteUtilisateur.class))).thenReturn(administrateur);
		doNothing().when(serviceMail).prepareAndSend(anyString(), anyString(), anyString());
		
		//GIVEN
		CompteUtilisateur compte = compteService.checkUserMailAndSaveUser(administrateur, role);
		
		//THEN
		verify(compteRepository).findByMail(mail);
		verify(compteRepository).save(administrateur);
		verify(serviceMail).prepareAndSend(eq(mail), eq(mail), anyString());
		verify(authorityRepository).save(any(Authority.class));
		assertThat(compte).isNotNull();
		assertThat(compte).isEqualToComparingFieldByField(administrateur);
	}
	
	
	@Test(expected=UserExistException.class)
	public void shouldCallCheckUserMailAndSaveUserThrowException() throws Exception {
		//WHEN
		String mail = "test@test.fr";
		String mdp = "test";
		String nom = "Dalton";
		String prenom = "Joe";
		String dateDeNaissance ="01/01/2000";
		String role = "ADMIN";
		Administrateur administrateur = new AdministrateurBuilder().avecMail(mail).avecMdp(mdp).avecNom(nom).avecPrenom(prenom).avecDateDeNaissance(dateDeNaissance).build();
		
		when(compteRepository.findByMail(anyString())).thenReturn(administrateur);
		
		//GIVEN
		compteService.checkUserMailAndSaveUser(administrateur, role);
		
		//THEN
		
	}
	
	
	@Test
	public void shouldFindAllComptes()
	{
		//WHEN
		List<CompteUtilisateur> listeComptes = new ArrayList<>();
		Etudiant etudiant = new EtudiantBuilder().avecRole(RoleCompteEnum.ETUDIANT.toString()).avecMail("etu@mail.fr").avecNom("Dalton").avecPrenom("Joe").avecDateDeNaissance("01/01/2000").avecMoyenneGenerale(17).avecNumeroEtudiant("20175406").build();
		Administrateur administrateur = new AdministrateurBuilder().avecRole(RoleCompteEnum.ADMINISTRATEUR.toString()).avecMail("adm@mail.fr").avecNom("Adm").avecPrenom("Adm").avecDateDeNaissance("01/01/2005").build();
		listeComptes.add(etudiant);
		listeComptes.add(administrateur);
		when(compteRepository.findAll()).thenReturn(listeComptes);
		
		//GIVEN
		List<CompteBean> listeComptesBeans = compteService.findAllComptes();
		
		//THEN
		assertThat(listeComptesBeans).isNotNull();
		assertThat(listeComptesBeans).hasSize(2);
		
	}
	@Test
	public void shouldGenerateMdp()
	{
		String mdp = compteService.generateMdp();
		assertThat(mdp).isNotNull();
		
	}


	@Test
	public void testListAllByPage() throws Exception {
		//WHEN
		Page<CompteUtilisateur> comptePage = buildPageUtilisateur();
		when(compteRepository.findAll(any(Pageable.class))).thenReturn(comptePage);
		//GIVEN
		compteService.listAllByPage(1);
		//THEN
		verify(compteRepository).findAll(any(Pageable.class));
	}
	
	@Test
	public void testDeleteCompteByMail() throws Exception {
		//WHEN
		String mail = "admin@hotmail.fr";
		Etudiant etudiant = new Etudiant();
		when(compteRepository.findByMail(anyString())).thenReturn(etudiant);
		doNothing().when(compteRepository).delete(any(CompteUtilisateur.class));
		//GIVEN
		compteService.deleteCompteByMail(mail);
		
		//THEN
		verify(compteRepository).findByMail(eq(mail));
		verify(compteRepository).delete(etudiant);
	}


	private Page<CompteUtilisateur> buildPageUtilisateur()
	{
		return new Page<CompteUtilisateur>() {

			@Override
			public List<CompteUtilisateur> getContent() {
				// TODO Auto-generated method stub
				return Arrays.asList(new AdministrateurBuilder().avecMail("admin@admin.fr").avecMdp("adm").avecNom("Admin").avecPrenom("Admin").avecDateDeNaissance("21/05/1994").avecRole(RoleCompteEnum.ADMINISTRATEUR.toString()).build());
			}

			@Override
			public int getNumber() {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public int getNumberOfElements() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getSize() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Sort getSort() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean hasContent() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasNext() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean hasPrevious() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isFirst() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isLast() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public Pageable nextPageable() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Pageable previousPageable() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public Iterator<CompteUtilisateur> iterator() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getTotalElements() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getTotalPages() {
				// TODO Auto-generated method stub
				return 10;
			}

			@Override
			public <S> Page<S> map(Converter<? super CompteUtilisateur, ? extends S> arg0) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}