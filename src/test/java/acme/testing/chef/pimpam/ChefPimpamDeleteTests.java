package acme.testing.chef.pimpam;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import acme.testing.TestHarness;

public class ChefPimpamDeleteTests extends TestHarness{

	@Test
	@Order(80)
	public void positivePimpamDeleteTest() {

		super.signIn("chef3", "chef3");
		super.clickOnMenu("Chef", "My Pimpams");

		super.checkListingExists();

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.checkSubmitExists("Delete");
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

	}

}
