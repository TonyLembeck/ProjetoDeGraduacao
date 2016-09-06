package br.com.android.sample.infrastructure.repository.types;

import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.SerializableType;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 *
 */
public class CalendarDataType extends SerializableType
{
	/**
	 *
	 */
	private static final CalendarDataType singleton = new CalendarDataType();

	/**
	 *
	 * @return
	 */
	public static CalendarDataType getSingleton()
	{
		return singleton;
	}

	/**
	 *
	 */
	private CalendarDataType()
	{
		super( SqlType.SERIALIZABLE, new Class<?>[]{Calendar.class} );
	}

	/**
	 * Here for others to subclass.
	 *
	 * @param sqlType
	 * @param classes
	 */
	protected CalendarDataType(SqlType sqlType, Class<?>[] classes)
	{
		super(sqlType, classes);
	}

	/**
	 *
	 * @return
     */
	@Override
	public String[] getAssociatedClassNames()
	{
		return new String[]{"java.util.Calendar"};
	}

	/**
	 *
	 * @param field
	 * @return
     */
	@Override
	public boolean isValidForField(Field field)
	{
		return Calendar.class.isAssignableFrom(field.getType());
	}

	/**
	 *
	 * @return
     */
	@Override
	public Class<?> getPrimaryClass()
	{
		return Calendar.class;
	}
}