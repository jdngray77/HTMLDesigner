namespace Utility.Core.Project.Model;

public class HTMLProj
{
    #region User configurable

    public string DisplayName;
    public string Description;
    public string Author;
    public string[] Tags;
    public double Version;
    
    #endregion

    #region Internal

    /// <summary>
    /// The version of the project structure.
    /// Changes with htmlproj changes or file structure changes.
    /// </summary>
    public double HTMLProjVersion;
    
    /// <summary>
    /// A list of all files known by the project.
    /// </summary>
    public List<string> Files;

    #endregion
}