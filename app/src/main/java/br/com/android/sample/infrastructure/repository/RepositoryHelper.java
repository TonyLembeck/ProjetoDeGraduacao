package br.com.android.sample.infrastructure.repository;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

import br.com.android.sample.domain.User;
import br.com.android.sample.infrastructure.repository.types.CalendarDataType;

/**
 * @created rodrigo
 */
public class RepositoryHelper extends OrmLiteSqliteOpenHelper
{
    /**
     *
     */
    private static final String DATABASE_NAME = "database.db";
    /**
     *
     */
    private static final int DATABASE_VERSION = 1;

    /*-------------------------------------------------------------------
	 * 		 					CONSTRUCTORS
	 *-------------------------------------------------------------------*/
    /**
     * @param context
     */
    public RepositoryHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        this.setupDataPersisters();
    }

    /*-------------------------------------------------------------------
	 * 		 					 BEHAVIORS
	 *-------------------------------------------------------------------*/

    /**
     * Custom data persisters
     */
    private void setupDataPersisters()
    {
        DataPersisterManager.registerDataPersisters(
                CalendarDataType.getSingleton()
        );
    }

    /**
     * @param database
     * @param connectionSource
     */
    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource)
    {
        try
        {
            TableUtils.dropTable( connectionSource, User.class, true );
            TableUtils.createTable( connectionSource, User.class );
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * @param database
     * @param connectionSource
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade( SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion )
    {
        System.out.println( oldVersion );
        System.out.println( newVersion );
    }
}