package acme.features.chef.suppa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.components.configuration.SystemConfigurationSep;
import acme.entities.recipes.Suppa;
import acme.entities.recipes.WareType;
import acme.features.authenticated.moneyExchangeSep.AuthenticatedMoneyExchangeSepPerformService;
import acme.features.authenticated.systemConfigurationSep.AuthenticatedSystemConfigurationSepRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Request;
import acme.framework.datatypes.Money;
import acme.framework.services.AbstractShowService;
import acme.roles.Chef;

@Service
public class ChefSuppaShowService implements AbstractShowService<Chef, Suppa>{

	@Autowired
	protected ChefSuppaRepository repository;
	
	@Autowired
	protected AuthenticatedSystemConfigurationSepRepository config;
	
	@Autowired
	protected AuthenticatedMoneyExchangeSepPerformService moneyExchange;
	
	protected Suppa suppa;
	
	@Override
	public boolean authorise(final Request<Suppa> request) {
		assert request != null;
		int chefId;
		Chef chef;
		int id;
		
		chefId = request.getPrincipal().getActiveRoleId();
		chef = this.repository.findOneChefById(chefId);
		id = request.getModel().getInteger("id");
		this.suppa = this.repository.findOneSuppaById(id);
		
		return this.suppa.getChef().equals(chef);
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
	public void unbind(final Request<Suppa> request, final Suppa entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		request.unbind(entity, model, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
		model.setAttribute("kitchenwareCode", entity.getKitchenware().getCode());
		model.setAttribute("kitchenwareName", entity.getKitchenware().getName());
		model.setAttribute("wareType", entity.getKitchenware().getWareType().name());
		model.setAttribute("IngredientType", WareType.INGREDIENT);
		model.setAttribute("KitchenUtensilType", WareType.KITCHEN_UTENSIL);
		this.unbindConvertedMoney(entity, model);
			
	}
	
	private void unbindConvertedMoney(final Suppa entity, final Model model) {
		final SystemConfigurationSep sc = this.config.findSystemConfiguration();
		final Money money = this.moneyExchange.computeMoneyExchange(entity.getBudget(), sc.getSystemCurrency()).getChange();
		model.setAttribute("budgetConverted", money);
	}
	

}
