package fr.bougly.web.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.format.Formatter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import fr.bougly.model.Administrateur;
import fr.bougly.model.CompteUtilisateur;
import fr.bougly.model.Enseignant;
import fr.bougly.model.Etudiant;
import fr.bougly.model.Responsable;
import fr.bougly.model.enumeration.RoleCompteEnum;
import fr.bougly.service.CompteService;
import fr.bougly.service.helper.MapperBeanUtil;
import fr.bougly.web.beans.CompteBean;


@Controller
@RequestMapping(value="/administrateur")
public class AdministrateurController {
	
	private static final String URL_CONTROLLEUR_ADMIN = "/administrateur";
	public static final String URL_GESTION_COMPTE_PAGE = "/gestionCompte.html";
	public static final String URL_CREER_COMPTE = "/creerCompte.html";
	public static final String URL_SUPPRIMER_COMPTE = "/supprimerCompte.html";
	public static final String URL_EDITER_COMPTE = "/editerCompte.html";
	
	@Autowired
	CompteService compteService;
	
	@RequestMapping(value=URL_GESTION_COMPTE_PAGE, method=RequestMethod.GET)
	public ModelAndView showPageGestionCompte(@RequestParam(defaultValue="1",required=true) Integer pageNumber)
	{
		Page<CompteUtilisateur> listeComptesPage = compteService.listAllByPage(pageNumber);
		List<CompteUtilisateur> listeComptes = listeComptesPage.getContent();
		ArrayList<CompteBean> listeComptesBean = MapperBeanUtil.convertListCompteToListCompteBean(listeComptes);
		int current = listeComptesPage.getNumber() + 1;
	    int begin = Math.max(1, current - 5);
	    int end = Math.min(begin + 10, listeComptesPage.getTotalPages());
		
		ModelAndView model = new ModelAndView("gestionCompte");
		
		model.addObject("listeComptes", listeComptesBean);
	    model.addObject("beginIndex", begin);
	    model.addObject("endIndex", end);
	    model.addObject("currentIndex", current);
		return model;
		
	}
	
	@RequestMapping(value=URL_CREER_COMPTE, method=RequestMethod.GET)
	public ModelAndView showPageCreerCompte()
	{
		
		ModelAndView model = new ModelAndView("creerCompte");
		model.addObject("compte", new CompteBean());
		model.addObject("allRoles",RoleCompteEnum.listesAllRoles());
		return model;
		
	}
	
	@RequestMapping(value=URL_CREER_COMPTE, method=RequestMethod.POST)
	public String creerCompteFromData(@ModelAttribute(value="compte") CompteBean compteBean) throws Exception
	{
		
		if(compteBean.getRole().equalsIgnoreCase(RoleCompteEnum.ADMINISTRATEUR.toString()))
		{
			Administrateur administrateur = new Administrateur(compteBean);
			compteService.checkUserMailAndSaveUser(administrateur, RoleCompteEnum.ADMINISTRATEUR.toString());
		}
		else if(compteBean.getRole().equalsIgnoreCase(RoleCompteEnum.RESPONSABLE.toString()))
		{
			Responsable responsable = new Responsable(compteBean);
			compteService.checkUserMailAndSaveUser(responsable, RoleCompteEnum.RESPONSABLE.toString());
		}
		else if(compteBean.getRole().equalsIgnoreCase(RoleCompteEnum.ENSEIGNANT.toString()))
		{
			Enseignant enseignant = new Enseignant(compteBean);
			compteService.checkUserMailAndSaveUser(enseignant, RoleCompteEnum.ENSEIGNANT.toString());
		}
		else
		{
			Etudiant etudiant = new Etudiant(compteBean);
			compteService.checkUserMailAndSaveUser(etudiant, RoleCompteEnum.ETUDIANT.toString());
		}
		
		return "redirect:"+URL_CONTROLLEUR_ADMIN+URL_GESTION_COMPTE_PAGE;
		
	}
	
	@RequestMapping(value=URL_SUPPRIMER_COMPTE, method=RequestMethod.POST)
	@ResponseBody
	public void supprimerCompte(@RequestParam(required=true) String mail)
	{
		compteService.deleteCompteByMail(mail);
		
	}
	
	//TODO A refactorer classe Controlleur parente
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		StringTrimmerEditor stringtrimmer = new StringTrimmerEditor(true);
		binder.registerCustomEditor(String.class, stringtrimmer);
	}
	
	@RequestMapping(value=URL_EDITER_COMPTE, method=RequestMethod.POST)
	@ResponseBody
	public String editerCompte(CompteBean compteBean)
	{
			compteService.editerCompteWithCompteBean(compteBean);
			return "OK";
	}

}
