package acme.features.chef.pimpam;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.OptionalInt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.recipes.Kitchenware;
import acme.entities.recipes.Pimpam;
import acme.entities.recipes.WareType;
import acme.features.authenticated.systemConfigurationSep.AuthenticatedSystemConfigurationSepRepository;
import acme.framework.components.models.Model;
import acme.framework.controllers.Errors;
import acme.framework.controllers.Request;
import acme.framework.services.AbstractCreateService;
import acme.roles.Chef;

@Service
public class ChefPimpamCreateService implements AbstractCreateService<Chef, Pimpam>{

	
	@Autowired
	protected ChefPimpamRepository repository;
	
	@Autowired
	protected AuthenticatedSystemConfigurationSepRepository config;
	
	protected Pimpam pimpam;
	
	@Override
	public boolean authorise(final Request<Pimpam> request) {
		assert request != null;
		return request.getPrincipal().hasRole(Chef.class);
	}
	

	@Override
	public void bind(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		request.bind(entity, errors, "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
		
		final String kitchenwareCode = String.valueOf(request.getModel().getAttribute("kitchenwareCode"));
		final Kitchenware kitchenware = this.repository.findOneKitchenwareByCode(kitchenwareCode);
		if(!errors.hasErrors("kitchenware")) {
			errors.state(request, kitchenware!=null, "*", "chef.pimpam.form.error.null_kitchenware");
		}
		if(kitchenware != null) {
			entity.setCode(this.codeFormat(kitchenware));
			entity.setKitchenware(kitchenware);
		}
		
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
			final Kitchenware kitchenware = entity.getKitchenware(); 
			model.setAttribute("kitchenwareCode", kitchenware.getCode());
		}
		
		request.unbind(entity, model, "instantiationMoment", "title", "description", "startDate",
			"finishDate", "budget", "link");
		model.setAttribute("readOnly", false);
		
	}


	private String codeFormat(final Kitchenware kitchenware) {
		final LocalDate now = LocalDate.now();
		final String year = String.valueOf(now.getYear());
		final String yearStr = year.substring(year.length() - 2);
		String month = String.valueOf(now.getMonthValue());
		String day = String.valueOf(now.getDayOfMonth());
		if(month.length() == 1) {
			month = "0" + month; 
		}
		
		day = String.valueOf(now.getDayOfMonth());
		if(day.length() == 1) {
			day = "0" + day; 
		}
		final Collection<String> pimpamCodes = this.repository.findManyPimpamCodesByKitchenwareId(kitchenware.getId());
		if(pimpamCodes == null || pimpamCodes.isEmpty()) {
			return month + "-" + day + "-" + yearStr + "/00";
		}
		final OptionalInt secondPartCodeOptInt = pimpamCodes.stream().filter(c -> this.isValidCode(c))
			.mapToInt(c-> Integer.parseInt(c.split("/")[1])).max();
		Integer secondPartCodeInt;
		if(secondPartCodeOptInt.isPresent()) {
			secondPartCodeInt = secondPartCodeOptInt.getAsInt() + 1;
		}
		else {
			return month + "-" + day + "-" + yearStr + "/00";
		}
		String secondPartCode = secondPartCodeInt.toString(); 
		if(secondPartCode.length() == 1) {
			secondPartCode = "0" + secondPartCode;
		}
		return month + "-" + day + "-" + yearStr + "/" + secondPartCode;
	}
	
	private boolean isValidCode(final String code) {
		
		final LocalDate now = LocalDate.now();
		String[] dateCode;
		dateCode = code.split("/")[0].split("-"); // 22/05/05 FRQ-2545
		
		String creationMomentYear;
		String creationMomentMonth;
		String creationMomentDay;
		creationMomentYear = String.valueOf(now.getYear());
		creationMomentYear = creationMomentYear.substring(creationMomentYear.length() - 2);
		
		creationMomentMonth = String.valueOf(now.getMonthValue());
		if(creationMomentMonth.length() == 1) {
			creationMomentMonth = "0" + creationMomentMonth; 
		}
		
		creationMomentDay = String.valueOf(now.getDayOfMonth());
		if(creationMomentDay.length() == 1) {
			creationMomentDay = "0" + creationMomentDay; 
		}
		
		return dateCode[0].equals(creationMomentMonth) && dateCode[1].equals(creationMomentDay) && 
			dateCode[2].equals(creationMomentYear);
	}

	@Override
	public Pimpam instantiate(final Request<Pimpam> request) {
		assert request != null;
		
		Chef chef;
		Date creationDate;
		final Calendar calendar;
		Date startDate;
		final Date finishDate;
	
		
		int chefId;
		chefId = request.getPrincipal().getActiveRoleId();
		chef = this.repository.findOneChefById(chefId);
		calendar = Calendar.getInstance();
		creationDate = calendar.getTime();
		calendar.setTime(creationDate);
		calendar.add(Calendar.SECOND, -1);
		startDate = new Date();
		finishDate = new Date();
		
		
		this.pimpam = new Pimpam();
		this.pimpam.setCode("");
		this.pimpam.setTitle("");
		this.pimpam.setDescription("");
		this.pimpam.setLink("");
		this.pimpam.setChef(chef);
		this.pimpam.setInstantiationMoment(creationDate);
		this.pimpam.setStartDate(startDate);
		this.pimpam.setFinishDate(finishDate);
				
		return this.pimpam;
	}

	@Override
	public void validate(final Request<Pimpam> request, final Pimpam entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
		
		if (!errors.hasErrors("code")) {
			this.pimpam = this.repository.findOnePimpamByCode(entity.getCode());
			errors.state(request, this.pimpam == null, "code", "chef.pimpam.form.error.duplicated_code");
		}
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
	public void create(final Request<Pimpam> request, final Pimpam entity) {
		assert request != null;
		assert entity != null;
		
		this.repository.save(entity);
		
	}

	

}
