using Utility.Core.Project.Model;

namespace Utility.Core.Project;

public class Project
{
    private int lastSaveHash;

    internal HTMLProj projFile;
    internal readonly Uri path;


    #region Forwarded properties    

    public string DisplayName
    {
        get => projFile.DisplayName;
        set => projFile.DisplayName = value;
    }

    public string Author
    {
        get => projFile.Author;
        set => projFile.Author = value;
    }

    public string Description
    {
        get => projFile.Description;
        set => projFile.Description = value;
    }

    public double Version
    {
        get => projFile.Version;
        set => projFile.Version = value;
    }

    public string[] Tags
    {
        get => projFile.Tags;
        set => projFile.Tags = value;
    }

    public string Path
    {
        get => path.LocalPath;
    }

    #endregion

    #region Paths

    internal ProjectPath HtmlProj { get; private set; }

    public ProjectPath PrefabsDir { get; private set; }
    public ProjectPath PagesDir { get; private set; }

    public ProjectPath AssetsDir { get; private set; }
    public ProjectPath StylesheetsDir { get; private set; }
    public ProjectPath ScriptsDir { get; private set; }
    public ProjectPath MediaDir { get; private set; }

    #endregion

    public Project(string name, Uri locationOnDisk)
        : this(name, string.Empty, locationOnDisk)
    {

    }    
    
    public Project(string name, string description, Uri locationOnDisk)
        : this(name, string.Empty, description, Array.Empty<string>(), double.NaN, locationOnDisk)
    {
    }

    public Project(string name, string description, string author, string[] tags, double version, Uri locationOnDisk)
    {
        projFile = new HTMLProj();
        this.path = locationOnDisk;
        DisplayName = name;
        Author = author;
        Tags = tags;
        Version = version;
        Description = description;
        FinalInit();
    }

    public Project(HTMLProj proj, Uri locationOnDisk)
    {
        this.projFile = proj;
        FinalInit();
    }

    public bool HasChangedSinceLastSaved()
    {
        return lastSaveHash != GetHashCode();
    }

    internal void NotifySaved()
    {
        lastSaveHash = GetHashCode();
    }

    public override bool Equals(object? obj)
    {
        return obj is Project project &&
               lastSaveHash == project.lastSaveHash &&
               EqualityComparer<HTMLProj>.Default.Equals(projFile, project.projFile) &&
               EqualityComparer<Uri>.Default.Equals(path, project.path) &&
               DisplayName == project.DisplayName &&
               Author == project.Author &&
               Description == project.Description &&
               Version == project.Version &&
               EqualityComparer<string[]>.Default.Equals(Tags, project.Tags) &&
               Path == project.Path &&
               EqualityComparer<ProjectPath>.Default.Equals(HtmlProj, project.HtmlProj) &&
               EqualityComparer<ProjectPath>.Default.Equals(PrefabsDir, project.PrefabsDir) &&
               EqualityComparer<ProjectPath>.Default.Equals(PagesDir, project.PagesDir) &&
               EqualityComparer<ProjectPath>.Default.Equals(AssetsDir, project.AssetsDir) &&
               EqualityComparer<ProjectPath>.Default.Equals(StylesheetsDir, project.StylesheetsDir) &&
               EqualityComparer<ProjectPath>.Default.Equals(ScriptsDir, project.ScriptsDir) &&
               EqualityComparer<ProjectPath>.Default.Equals(MediaDir, project.MediaDir);
    }

    public override int GetHashCode()
    {
        HashCode hash = new HashCode();
        hash.Add(lastSaveHash);
        hash.Add(projFile);
        hash.Add(path);
        hash.Add(DisplayName);
        hash.Add(Author);
        hash.Add(Description);
        hash.Add(Version);
        hash.Add(Tags);
        hash.Add(Path);
        hash.Add(HtmlProj);
        hash.Add(PrefabsDir);
        hash.Add(PagesDir);
        hash.Add(AssetsDir);
        hash.Add(StylesheetsDir);
        hash.Add(ScriptsDir);
        hash.Add(MediaDir);
        return hash.ToHashCode();
    }

    private void FinalInit()
    {
        HtmlProj = new ProjectPath(this, "htmlproj.xml");

        PagesDir = new ProjectPath(this, "Pages/");

        AssetsDir = new ProjectPath(this, "Assets/");
        StylesheetsDir = new ProjectPath(AssetsDir, "Stylesheets/");
        ScriptsDir = new ProjectPath(AssetsDir, "Scripts/");
        MediaDir = new ProjectPath(AssetsDir, "Media/");
        PrefabsDir = new ProjectPath(AssetsDir, "Prefabs/");

        NotifySaved();
    }
}