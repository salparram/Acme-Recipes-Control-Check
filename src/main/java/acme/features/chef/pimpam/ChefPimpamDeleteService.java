package acme.features.chef.pimpam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Pimpam;
import acme.entities.recipes.WareType;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Chef;

@Service
public class ChefPimpamDeleteService implements AbstractDeleteService<Chef, Pimpam>{

	@Autowired
	protected ChefPimpamRepository repository;
	
	protected Pimpam pimpam;
	
	@Override
	public boolean authorise(final Request<Pimpam> request) {
		assert request != null;
		int id;
		int chefId;
		Chef chef;
		
		chefId = request.getPrincipal().getActiveRoleId();
		chef = this.repository.findOneChefById(chefId);
		id = request.getModel().getInteger("id");
		this.pimpam = this.repository.findOnePimpamById(id);
		final Kitchenware kitchenware = this.pimpam.getKitchenware(); 
		
		return this.pimpam.getChef().equals(chef) && 
			kitchenware.getWareType().equals(WareType.INGREDIENT) && !kitchenware.isPublished();
	}

	@Override
	public void bind(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
	}

	@Override
	public void unbind(final Request<Pimpam> request, final Pimpam entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
		
		model.setAttribute("kitchenwareCode", entity.getKitchenware().getCode());
		model.setAttribute("kitchenwareName", entity.getKitchenware().getName());
		model.setAttribute("wareType", entity.getKitchenware().getWareType().name());
		
	}

	@Override
	public Pimpam findOne(final Request<Pimpam> request) {
		assert request != null;
		int id;
		id = request.getModel().getInteger("id");
		this.pimpam = this.repository.findOnePimpamById(id);
		
		return this.pimpam;
	}

	@Override
	public void validate(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		if(!errors.hasErrors("kitchenware")) {
			errors.state(request, entity.getKitchenware().getWareType().equals(WareType.INGREDIENT), 
				"*", "chef.pimpam.form.error.must-be-ingredient");
			/*errors.state(request, !entity.getKitchenware().getWareType().equals(WareType.KITCHEN_UTENSIL), 
				"*", "chef.pimpam.form.error.must-be-kitchenUtensil");*/
		}
		
	}

	@Override
	public void delete(final Request<Pimpam> request, final Pimpam entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.delete(entity);
		
	}

	

}
