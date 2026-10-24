
/* -----------------------------
 * Category class created by Tiana
   ----------------------------- */

package entityClasses;

public enum Category {
	General,
	Technical,
	Teamwork,
	Project_Management,
	Communication,
	Research,
	Career;

	/*****
	 * <p> Method: Category fromString(String text) </p>
	 * 
	 * <p> Description: Converts text into a category.  The match is exact (case-sensitive) so the
	 * stored values never vary in spelling.  This method never throws an exception, so the caller
	 * can produce a helpful error message instead. </p>
	 * 
	 * @param text specifies the category name to look up; may be null
	 * 
	 * @return the matching category, or null if the text is null or is not a category name
	 */
	
	public static Category fromString(String text) {
		if (text == null) return null;
		for (Category c : Category.values()) {
			if (c.name().equals(text)) return c;
		}
		return null;
	}
}