package acme.features.chef.suppa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Suppa;
import acme.entities.recipes.WareType;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractDeleteService;
import acme.roles.Chef;

@Service
public class ChefSuppaDeleteService implements AbstractDeleteService<Chef, Suppa>{

	@Autowired
	protected ChefSuppaRepository repository;
	
	protected Suppa suppa;
	
	@Override
	public boolean authorise(final Request<Suppa> request) {
		assert request != null;
		int id;
		int chefId;
		Chef chef;
		
		chefId = request.getPrincipal().getActiveRoleId();
		chef = this.repository.findOneChefById(chefId);
		id = request.getModel().getInteger("id");
		this.suppa = this.repository.findOneSuppaById(id);
		final Kitchenware kitchenware = this.suppa.getKitchenware(); 
		
		return this.suppa.getChef().equals(chef) && 
			kitchenware.getWareType().equals(WareType.INGREDIENT) && !kitchenware.isPublished();
	}

	@Override
	public void bind(final Request<Suppa> request, final Suppa entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
	}

	@Override
	public void unbind(final Request<Suppa> request, final Suppa entity, final Model model) {
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
	public Suppa findOne(final Request<Suppa> request) {
		assert request != null;
		int id;
		id = request.getModel().getInteger("id");
		this.suppa = this.repository.findOneSuppaById(id);
		
		return this.suppa;
	}

	@Override
	public void validate(final Request<Suppa> request, final Suppa entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		if(!errors.hasErrors("kitchenware")) {
			errors.state(request, entity.getKitchenware().getWareType().equals(WareType.INGREDIENT), 
				"*", "chef.suppa.form.error.must-be-ingredient");
			/*errors.state(request, !entity.getKitchenware().getWareType().equals(WareType.KITCHEN_UTENSIL), 
				"*", "chef.suppa.form.error.must-be-kitchenUtensil");*/
		}
		
	}

	@Override
	public void delete(final Request<Suppa> request, final Suppa entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.delete(entity);
		
	}

	

}
