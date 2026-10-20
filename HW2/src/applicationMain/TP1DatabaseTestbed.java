package applicationMain;

import java.util.List;

import database.Database;
import entityClasses.User;
import passwordRecognizer.PasswordRecognizer;

/**
 * Semiautomated integration tests for TP1 database-backed functionality.
 * Uses a private in-memory H2 database and never touches the normal application database.
 */
public final class TP1DatabaseTestbed {
	private static int passed;
	private static int failed;

	private TP1DatabaseTestbed() { }

	private static void check(String name, boolean condition) {
		if (condition) {
			passed++;
			System.out.println("PASS: " + name);
		} else {
			failed++;
			System.out.println("FAIL: " + name);
		}
	}

	private static User user(String username, String password, boolean admin,
			boolean contributorRole, boolean curatorRole) {
		return new User(username, password, "Test", "", "User", "", username
				+ "@asu.edu", admin, contributorRole, curatorRole);
	}

	public static void main(String[] args) throws Exception {
		System.setProperty("foundation.db.url",
				"jdbc:h2:mem:tp1_test;DB_CLOSE_DELAY=-1");
		Database database = new Database();
		database.connectToDatabase();
		try {
			database.register(user("adminTest", "Admin123!", true, false, false));
			database.register(user("userTest", "OldPass1!", false, true, false));

			check("two registered users", database.getNumberOfUsers() == 2);
			check("account listing contains both users",
					database.getUserAccountsList().size() == 2);
			check("existing user lookup", database.doesUserExist("userTest"));
			check("missing user lookup", !database.doesUserExist("missing"));

			String code = database.generateInvitationCode("invite@asu.edu", "curatorRole");
			check("invitation code generated", code != null && code.length() == 6);
			check("invitation count", database.getNumberOfInvitations() == 1);
			check("invitation email stored",
					"invite@asu.edu".equals(database.getEmailAddressUsingCode(code)));
			check("invitation role stored",
					"curatorRole".equals(database.getRoleGivenAnInvitationCode(code)));
			check("outstanding invitation listed", contains(database.getInvitationList(), code));
			database.removeInvitationAfterUse(code);
			check("used invitation removed", database.getNumberOfInvitations() == 0);

			check("role added", database.updateUserRole("userTest", "curatorRole", "true"));
			check("updated role persisted", database.getUserAccountDetails("userTest")
					&& database.getCurrentCuratorRole());
			check("role removed", database.updateUserRole("userTest", "curatorRole", "false"));
			check("removed role persisted", database.getUserAccountDetails("userTest")
					&& !database.getCurrentCuratorRole());

			check("OTP rejects missing user",
					!database.setOneTimePassword("missing", "TempPass1!"));
			check("OTP rejects empty value", !database.setOneTimePassword("userTest", ""));
			check("OTP rejects oversized value", !database.setOneTimePassword("userTest",
					"A".repeat(PasswordRecognizer.MAX_LENGTH + 1)));
			check("OTP assigned", database.setOneTimePassword("userTest", "TempPass1!"));
			check("reset flag stored", database.getUserAccountDetails("userTest")
					&& database.getCurrentIsOneTimePassword());
			check("password cannot save before OTP consumption",
					!database.saveResetPassword("userTest", "NewPass1!"));
			check("wrong OTP rejected",
					!database.consumeOneTimePassword("userTest", "WrongPass1!"));
			check("correct OTP consumed",
					database.consumeOneTimePassword("userTest", "TempPass1!"));
			check("OTP replay rejected",
					!database.consumeOneTimePassword("userTest", "TempPass1!"));
			check("new password saved",
					database.saveResetPassword("userTest", "NewPass1!"));
			check("new password persisted", database.getUserAccountDetails("userTest")
					&& "NewPass1!".equals(database.getCurrentPassword()));
			check("reset flag cleared", !database.getCurrentIsOneTimePassword());

			check("ordinary user deleted", database.deleteUser("userTest"));
			check("deleted user absent", !database.doesUserExist("userTest"));
			check("second deletion rejected", !database.deleteUser("userTest"));
		} finally {
			database.closeConnection();
		}

		System.out.println("TP1 database tests passed: " + passed + "; failed: " + failed);
		if (failed > 0) throw new AssertionError(failed + " TP1 database test(s) failed");
	}

	private static boolean contains(List<String> values, String expectedText) {
		return values != null && values.stream().anyMatch(value -> value.contains(expectedText));
	}
}
