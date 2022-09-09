package acme.testing.chef.suppa;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import acme.testing.TestHarness;

public class ChefSuppaDeleteTests extends TestHarness{

	@Test
	@Order(80)
	public void positiveSuppaDeleteTest() {

		super.signIn("chef3", "chef3");
		super.clickOnMenu("Chef", "My Suppas");

		super.checkListingExists();

		super.clickOnListingRecord(0);
		super.checkFormExists();
		super.checkSubmitExists("Delete");
		super.clickOnSubmit("Delete");
		super.checkNotErrorsExist();

	}

}
