package robor.wildfiremapper.data.db;

import javax.inject.Inject;
import javax.inject.Singleton;

import robor.wildfiremapper.data.db.model.DaoMaster;
import robor.wildfiremapper.data.db.model.DaoSession;

/**
 * The job of this class is to manage the database and all data handling related to a database.
 */
@Singleton
public class AppDbHelper implements DbHelper {

    private final DaoSession daoSession;

    @Inject
    public AppDbHelper(DbOpenHelper dbOpenHelper) {
        daoSession = new DaoMaster(dbOpenHelper.getWritableDb()).newSession();
    }

}
