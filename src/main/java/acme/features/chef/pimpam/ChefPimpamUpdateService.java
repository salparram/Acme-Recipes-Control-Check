package acme.features.chef.pimpam;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Pimpam;
import acme.entities.recipes.WareType;
import acme.features.authenticated.systemConfigurationSep.AuthenticatedSystemConfigurationSepRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractUpdateService;
import acme.roles.Chef;

@Service
public class ChefPimpamUpdateService implements AbstractUpdateService<Chef, Pimpam>{
	
	@Autowired
	protected ChefPimpamRepository repository;
	
	@Autowired
	protected AuthenticatedSystemConfigurationSepRepository config;
	
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
		
		return this.pimpam.getChef().equals(chef);
	}

	@Override
	public void bind(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "title", "description", "startDate",
			"finishDate", "budget", "link");
		
		final String kitchenwareCode = String.valueOf(request.getModel().getAttribute("kitchenwareCode"));
		final Kitchenware kitchenware = this.repository.findOneKitchenwareByCode(kitchenwareCode);
		if(!errors.hasErrors("kitchenware")) {
			errors.state(request, kitchenware!=null, "*", "chef.pimpam.form.error.null_kitchenware");
		}
		entity.setKitchenware(kitchenware);
		
	}

	@Override
	public void unbind(final Request<Pimpam> request, final Pimpam entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		
		if(entity.getKitchenware() == null) {
			model.setAttribute("kitchenwareCode", "");
		}
		else {
			model.setAttribute("kitchenwareCode", entity.getKitchenware().getCode());
		}
		
		request.unbind(entity, model, "code", "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
		
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
		
		if(!errors.hasErrors("startDate")) {
			Date minimumDate;
			
			Calendar calendar;
			calendar = new GregorianCalendar();
			calendar.add(Calendar.MONTH, 1);
			minimumDate = calendar.getTime();
			
			errors.state(request, entity.getStartDate().after(minimumDate), "startDate", "chef.pimpam.form.error.invalid_startDate");
		}
		
		if(!errors.hasErrors("finishDate") && entity.getStartDate() != null) {
			Calendar calendar;
			Date minimunDate;
			
			calendar = new GregorianCalendar();
			calendar.setTime(entity.getStartDate());
			
			calendar.add(Calendar.WEEK_OF_YEAR, 1);
			minimunDate = calendar.getTime();
			
			errors.state(request, entity.getFinishDate().after(minimunDate), "finishDate", "chef.pimpam.form.error.invalid_finishDate");
		}
		
		if(!errors.hasErrors("budget")) {
			final String entityCurrency = entity.getBudget().getCurrency();
			final Double amount = entity.getBudget().getAmount();
			errors.state(request, amount > 0, "budget", "chef.pimpam.form.error.negative");
			
			final List<String> currencies= new ArrayList<String>();
			final String[] acceptedCurrencies=this.config.findAcceptedCurrencies().split(",");
			for (final String currency : acceptedCurrencies){
			    currencies.add(currency);
			    }
			errors.state(request, currencies.contains(entityCurrency) , "budget", "chef.pimpam.form.error.noAcceptedCurrency");
		}
		
		if(!errors.hasErrors("kitchenware")) {
			final Kitchenware kitchenware = entity.getKitchenware();
			final int chefId = request.getPrincipal().getActiveRoleId();
			final Chef chef = this.repository.findOneChefById(chefId); 
			errors.state(request, chef.equals(kitchenware.getChef()), "*", "chef.pimpam.form.error.notYourKitchenware");
			errors.state(request, !kitchenware.isPublished(), "*", "chef.pimpam.form.error.notPublishedKitchenware");
			errors.state(request, entity.getKitchenware().getWareType().equals(WareType.INGREDIENT), 
				"*", "chef.pimpam.form.error.must-be-ingredient");
			/*errors.state(request, !entity.getKitchenware().getWareType().equals(WareType.KITCHEN_UTENSIL), 
				"*", "chef.pimpam.form.error.must-be-kitchenUtensil");*/
		}
		
	}

	@Override
	public void update(final Request<Pimpam> request, final Pimpam entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);
		
	}

	

}
