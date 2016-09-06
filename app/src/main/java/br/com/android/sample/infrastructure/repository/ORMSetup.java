package br.com.android.sample.infrastructure.repository;

import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import br.com.android.sample.domain.User;


/**
 *
 */
public class ORMSetup extends OrmLiteConfigUtil
{
    /**
     *
     */
    private static final Class<?>[] classes = new Class[] {
            User.class,
    };

    /**
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        OrmLiteConfigUtil.writeConfigFile("ormlite_config.txt", classes);
    }
}