package acme.testing.chef.pimpam;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import acme.testing.TestHarness;

public class ChefPimpamListAndShowTests extends TestHarness{

	@ParameterizedTest
	@CsvFileSource(resources = "/chef/pimpam/list.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)
	public void positiveChefPimpamTest(final int recordIndex, final String code, final String instantiationMoment, final String title, 
		final String description, final String startDate, final String finishDate, final String budget, final String convertedBudget, 
		final String link, final String kitchenwareCode, final String kitchenwareName,
		final String kitchenwareType) {
		
		super.signIn("chef1", "chef1");
		
		super.clickOnMenu("Chef", "My Pimpams");	
		super.checkListingExists();
		super.sortListing(0, "desc");

		super.checkColumnHasValue(recordIndex, 0, instantiationMoment);
		super.checkColumnHasValue(recordIndex, 1, code);
		super.checkColumnHasValue(recordIndex, 2, title);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists(); 
		super.checkInputBoxHasValue("code", code);
		super.checkInputBoxHasValue("title", title); 
		super.checkInputBoxHasValue("instantiationMoment", instantiationMoment);
		super.checkInputBoxHasValue("description", description); 
		super.checkInputBoxHasValue("startDate", startDate); 
		super.checkInputBoxHasValue("finishDate", finishDate); 
		super.checkInputBoxHasValue("budget", budget);
		super.checkInputBoxHasValue("budgetConverted", convertedBudget);
		super.checkInputBoxHasValue("link", link);
		super.checkInputBoxHasValue("kitchenwareName", kitchenwareName);
		super.checkInputBoxHasValue("kitchenwareCode", kitchenwareCode);
		super.checkInputBoxHasValue("wareType", kitchenwareType);
		
		super.signOut();
		
		
	}
	
	@Test
	@Order(20)
	public void hackingTest() {
		final String pathShowKitchenware= "/chef/pimpam/show";
		
		
		super.checkNotLinkExists("Chef");
		super.navigate(pathShowKitchenware);
		super.checkPanicExists();
		
		super.signIn("administrator", "administrator");
		super.checkNotLinkExists("Chef");
		super.navigate(pathShowKitchenware);
		super.checkPanicExists();
		super.signOut();
		
		super.signIn("epicure1", "epicure1");
		super.checkNotLinkExists("Chef");
		super.navigate(pathShowKitchenware);
		super.checkPanicExists();
		
		super.signOut();
		
	}
}
