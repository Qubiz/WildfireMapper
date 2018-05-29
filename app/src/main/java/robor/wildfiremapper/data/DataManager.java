package robor.wildfiremapper.data;

import robor.wildfiremapper.data.db.DbHelper;
import robor.wildfiremapper.data.network.ApiHelper;
import robor.wildfiremapper.data.prefs.PreferencesHelper;

/**
 * Interface implemented by the AppDataManager that contains the methods exposed to the
 * different application components. This layer decouples any specific implementation of the
 * DataManager and hence makes AppDataManager as plug and play unit.
 */
public interface DataManager extends DbHelper, PreferencesHelper, ApiHelper {

}
