package club.magicfun.aquila.converter;

import java.beans.PropertyEditorSupport;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DefaultDateEditor extends PropertyEditorSupport {

	private final DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	public String getAsText() {
		Date value = (Date) getValue();
		return (value != null ? dateFormat1.format(value) : null);
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		if (text == null || text.length() == 0) {
			this.setValue(null);
		} else {
			DateFormat dateFormat = null;

			if (text.length() == 19) {
				dateFormat = dateFormat1;
				try {
					setValue(dateFormat.parse(text));
				} catch (ParseException ex) {
					throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
				}
			} else if (text.length() == 10) {
				dateFormat = dateFormat2;
				try {
					setValue(dateFormat.parse(text));
				} catch (ParseException ex) {
					throw new IllegalArgumentException("Could not parse date: " + ex.getMessage(), ex);
				}
			} else {
				throw new IllegalArgumentException("Could not parse date");
			}
		}
	}

}
