namespace Utility.Core.Project.Model;

internal class HTMLProj
{
    #region User configurable

    public string DisplayName;
    public string Author;
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