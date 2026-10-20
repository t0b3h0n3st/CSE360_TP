package passwordRecognizer;

/** Semiautomated boundary and rule tests for the shared password recognizer. */
public final class PasswordRecognizerTestbed {
	private static int passed;
	private static int failed;

	private PasswordRecognizerTestbed() { }

	private static void check(String name, boolean condition) {
		if (condition) {
			passed++;
			System.out.println("PASS: " + name);
		} else {
			failed++;
			System.out.println("FAIL: " + name);
		}
	}

	public static void main(String[] args) {
		check("valid password", PasswordRecognizer.evaluatePassword("Test123!").isEmpty());
		check("exact minimum length", PasswordRecognizer.evaluatePassword("Exctly8!").isEmpty());
		check("exact maximum length", PasswordRecognizer
				.evaluatePassword("Exactly32!CharactersInLengthTest").isEmpty());
		check("empty password", !PasswordRecognizer.evaluatePassword("").isEmpty());
		check("missing uppercase", !PasswordRecognizer.evaluatePassword("test123!").isEmpty());
		check("missing lowercase", !PasswordRecognizer.evaluatePassword("TEST123!").isEmpty());
		check("missing digit", !PasswordRecognizer.evaluatePassword("TestPass!").isEmpty());
		check("missing special", !PasswordRecognizer.evaluatePassword("Test1234").isEmpty());
		check("invalid whitespace", !PasswordRecognizer.evaluatePassword("Test 123!").isEmpty());
		check("over maximum length", !PasswordRecognizer
				.evaluatePassword("moreThan_ThirtyTwo_CharactersTest!").isEmpty());
		System.out.println("Password tests passed: " + passed + "; failed: " + failed);
		if (failed > 0) throw new AssertionError(failed + " password test(s) failed");
	}
}
