using System.Runtime.CompilerServices;
using Utility.Core.Project.Model;

namespace Utility.Core.Project;

public class Project
{
    internal HTMLProj projFile;
    public readonly Uri locationOnDisk;

    #region Paths

    internal readonly ProjectPath HtmlProj;
    
    public readonly ProjectPath PrefabsDir;
    public readonly ProjectPath PagesDir;

    public readonly ProjectPath AssetsDir;
    public readonly ProjectPath StylesheetsDir;
    public readonly ProjectPath ScriptsDir;
    public readonly ProjectPath MediaDir;

    #endregion

    public Project(Uri locationOnDisk)
     : this(string.Empty, locationOnDisk)
    {
    }
    
    public Project(string name, Uri locationOnDisk)
        : this(name, string.Empty, locationOnDisk)
    {
    }

    public Project(string name, string author, Uri locationOnDisk)
    {
        projFile = new HTMLProj();
        projFile.DisplayName = name;
        projFile.Author = name;

        this.locationOnDisk = locationOnDisk;

        HtmlProj = new ProjectPath(this, "htmlproj.xml");
        
        PagesDir = new ProjectPath(this, "Pages/");
        PagesDir = new ProjectPath(this, "Prefabs/");

        AssetsDir = new ProjectPath(this, "Assets/");
        StylesheetsDir = new ProjectPath(AssetsDir, "Stylesheets/");
        ScriptsDir = new ProjectPath(AssetsDir, "Scripts/");
        MediaDir = new ProjectPath(AssetsDir, "Media/");
    }
}