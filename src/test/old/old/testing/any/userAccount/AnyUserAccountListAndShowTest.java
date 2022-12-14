
package old.testing.any.userAccount;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.springframework.core.annotation.Order;

import old.testing.TestHarness;


public class AnyUserAccountListAndShowTest extends TestHarness {

	@ParameterizedTest
	@CsvFileSource(resources = "/any/user-account/user-account.csv", encoding = "utf-8", numLinesToSkip = 1)
	@Order(10)

	public void positiveUserAccountTest(final int recordIndex, final String name,
		final String surname, final String email, final String password, final String username) {

		super.navigateHome();
		super.clickOnMenu("Anonymous", "User Accounts");
		
		super.checkListingExists();
		super.sortListing(0, "asc");
		
		super.checkColumnHasValue(recordIndex, 0, name);
		super.checkColumnHasValue(recordIndex, 1, surname);

		super.clickOnListingRecord(recordIndex);
		super.checkFormExists();
		super.checkInputBoxHasValue("identity.name", name);
		super.checkInputBoxHasValue("identity.surname", surname);
		super.checkInputBoxHasValue("identity.email", email);
		
	}
	
	
}
