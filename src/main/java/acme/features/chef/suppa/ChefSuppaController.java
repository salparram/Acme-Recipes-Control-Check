package acme.features.chef.suppa;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import acme.entities.recipes.Suppa;
import acme.framework.controllers.AbstractController;
import acme.roles.Chef;

@Controller
public class ChefSuppaController extends AbstractController<Chef, Suppa>{

	@Autowired
	protected ChefSuppaListService listService;
	
	@Autowired
	protected ChefSuppaShowService showService;
	
	@Autowired
	protected ChefSuppaCreateService createService;
	
	@Autowired
	protected ChefSuppaUpdateService updateService;
	
	@Autowired
	protected ChefSuppaDeleteService deleteService;
	

	@PostConstruct
	protected void initialise() {
		super.addCommand("list", this.listService);
		super.addCommand("show", this.showService);
		super.addCommand("create", this.createService);
		super.addCommand("update", this.updateService);
		super.addCommand("delete", this.deleteService);
	}

}
