using Services.Interfaces;
using Services.Interfaces.Project;
using Utility.Core.Project;
using Utility.Core.Project.Model;

namespace Services.Core;

public class ProjectManagementService : IProjectManagementService
{
    private ISerializationService serializer;
    private IPreferencesService prefs;

    private List<string> projectHistory = new List<string>();

    public ProjectManagementService(
        ISerializationService serializer,
        IPreferencesService prefs)
    {
        this.serializer = serializer;
        this.prefs = prefs;
        LoadHistory();
    }

    /// <summary>
    /// Creates a new html project structure on disk.
    /// 
    /// Otherwise, the directory structure is created and the project xml metadata
    /// is saved to disk.
    /// </summary>
    /// <param name="project">An object representing the newly created project.</param>
    /// <returns></returns>
    /// <exception cref="Exception">If the desired local path is already taken. This method has no effect.</exception>
    public Project CreateNewProject(Project project)
    {
        if (File.Exists(project.HtmlProj.LocalPath))
        {
            throw new Exception("Project already exists");
        }

        TouchProject(project);
        CreateProjectDirectoryStructure(project);
        SaveProjFile(project);
        return project;
    }

    /// <summary>
    /// Saves the htmlproj.xml metadata file to disk,
    /// overriting any existing version.
    /// 
    /// Use this to stow any changes made to the metadata.
    /// </summary>
    /// <param name="project"></param>
    public void SaveProjFile(Project project)
    {
        TouchProject(project);
        serializer.SerializeModelToDisk(project.projFile, project.HtmlProj);
        project.NotifySaved();
    }

    public void SaveProjFileIfChanged(Project project)
    {
        if (project.HasChangedSinceLastSaved())
        {
            SaveProjFile(project);
        }
    }

    public Project LoadProject(string uri)
    {
        return LoadProject(new Uri(uri));
    }

    public Project LoadProject(Uri uri)
    {
        HTMLProj projfile = serializer.DeserializeModelFromDisk<HTMLProj>(uri);
        Project project = new Project(projfile, uri);
        TouchProject(project);
        return project;
    }

    public IEnumerable<string> GetRecentProjects()
    {
        return projectHistory.AsReadOnly();
    }

    /// <summary>
    /// Logs a project as having been touched recently.
    /// 
    /// Project is moved to the top of the history list.
    /// </summary>
    private void TouchProject(Project project)
    {
        if (projectHistory.Contains(project.Path))
        {
            projectHistory.Remove(project.Path);
        }

        projectHistory.Add(project.Path);
    }

    private void LoadHistory()
    {
        string historyxml = prefs.GetPreference("Core.ProjectManagementService.History");
        if (string.IsNullOrEmpty(historyxml))
        {
            projectHistory = serializer.DeserializeModel<List<string>>(historyxml);
        }
    }

    private void SaveHistory()
    {
        prefs.SetPreference("Core.ProjectManagementService.History", serializer.SerializeModel(projectHistory));
    }

    private void CreateProjectDirectoryStructure(Project project)
    {
        Directory.CreateDirectory(project.Path.ToString());

        Directory.CreateDirectory(project.PagesDir.LocalPath);
        Directory.CreateDirectory(project.PrefabsDir.LocalPath);
        Directory.CreateDirectory(project.AssetsDir.LocalPath);
        Directory.CreateDirectory(project.StylesheetsDir.LocalPath);
        Directory.CreateDirectory(project.ScriptsDir.LocalPath);
        Directory.CreateDirectory(project.MediaDir.LocalPath);
    }
}