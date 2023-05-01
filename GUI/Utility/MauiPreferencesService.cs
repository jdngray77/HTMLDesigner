using Services.Interfaces;

namespace HTML_Designer_MAUI.Utility
{
    internal class MauiPreferencesService : IPreferencesService
    {
        private IPreferences preferences;

        public MauiPreferencesService()
        {
            this.preferences = preferences;
        }

        public string GetPreference(string key)
        {
            return preferences.Get(key, string.Empty);
        }

        public void SetPreference(string key, string value)
        {
            preferences.Set(key, value);
        }
    }
}
