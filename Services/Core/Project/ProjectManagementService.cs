using Services.Interfaces;
using Utility.Core.Project;

namespace Services.Core;

public class ProjectManagementService
{
    private ISerializationService serializer;

    public ProjectManagementService(ISerializationService serializer)
    {
        this.serializer = serializer;
    }

    public Project CreateNewProject(string name, string author, Uri uri)
    {
        Project project = new Project(name, author, uri);
        CreateProjectDirectoryStructure(project);
        SaveProjFile(project);
        return project;
    }

    public void SaveProjFile(Project project)
    {
        serializer.SerializeModelToDisk(project.projFile, project.locationOnDisk);
    }

    private void CreateProjectDirectoryStructure(Project project)
    {
        Directory.CreateDirectory(project.locationOnDisk.ToString());

        Directory.CreateDirectory(project.PagesDir.ToString());
        Directory.CreateDirectory(project.PrefabsDir.ToString());
        Directory.CreateDirectory(project.AssetsDir.ToString());
        Directory.CreateDirectory(project.StylesheetsDir.ToString());
        Directory.CreateDirectory(project.ScriptsDir.ToString());
        Directory.CreateDirectory(project.MediaDir.ToString());
    } 
}