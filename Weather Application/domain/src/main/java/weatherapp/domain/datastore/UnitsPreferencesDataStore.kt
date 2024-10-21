package weatherapp.domain.datastore

import kotlinx.coroutines.flow.Flow
import weatherapp.domain.models.PreferenceUnits

interface UnitsPreferencesDataStore {

    var preferencesUnits: Flow<PreferenceUnits>

    suspend fun savePreferencesUnits(preferenceUnits: PreferenceUnits)

}