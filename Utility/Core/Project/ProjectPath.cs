namespace Utility.Core.Project;

/// <summary>
/// A URI relative to the given project.
/// </summary>
public class ProjectPath : Uri
{
    public ProjectPath(Project project, string? relativeUri) 
        : base(project.path, relativeUri)
    {
    }
    
    public ProjectPath(ProjectPath relativeTo, string? relativeUri) 
        : base(relativeTo, relativeUri)
    {
    }
}