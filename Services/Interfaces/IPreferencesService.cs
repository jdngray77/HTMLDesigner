namespace Services.Interfaces
{
    public interface IPreferencesService
    {
        public string GetPreference(string key);
        public void SetPreference(string key, string value);
    }
}
