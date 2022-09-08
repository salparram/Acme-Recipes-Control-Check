package acme.features.chef.pimpam;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Pimpam;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Chef;

@Service
public class ChefPimpamListService implements AbstractListService<Chef, Pimpam>{
	
	@Autowired
	protected ChefPimpamRepository repository;
	
	protected Collection<Pimpam> pimpams;

	@Override
	public boolean authorise(final Request<Pimpam> request) {
		assert request != null;
		return request.getPrincipal().hasRole(Chef.class);
	}

	@Override
	public Collection<Pimpam> findMany(final Request<Pimpam> request) {
		assert request != null;
		int chefId;
		chefId = request.getPrincipal().getActiveRoleId();
		this.pimpams = this.repository.findManyPimpamsByChefId(chefId);
		return this.pimpams;
	}

	@Override
	public void unbind(final Request<Pimpam> request, final Pimpam entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link", "kitchenware.name");
		
	}

	

}
