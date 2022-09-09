package acme.features.chef.suppa;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Suppa;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractListService;
import acme.roles.Chef;

@Service
public class ChefSuppaListService implements AbstractListService<Chef, Suppa>{
	
	@Autowired
	protected ChefSuppaRepository repository;
	
	protected Collection<Suppa> suppas;

	@Override
	public boolean authorise(final Request<Suppa> request) {
		assert request != null;
		return request.getPrincipal().hasRole(Chef.class);
	}

	@Override
	public Collection<Suppa> findMany(final Request<Suppa> request) {
		assert request != null;
		int chefId;
		chefId = request.getPrincipal().getActiveRoleId();
		this.suppas = this.repository.findManySuppasByChefId(chefId);
		return this.suppas;
	}

	@Override
	public void unbind(final Request<Suppa> request, final Suppa entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link", "kitchenware.name");
		
	}

	

}
