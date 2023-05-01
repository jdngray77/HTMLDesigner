using CoreProject = Utility.Core.Project.Project;

namespace Services.Interfaces.Project
{
    public interface IProjectManagementService
    {
        public CoreProject CreateNewProject(CoreProject project);

        public void SaveProjFile(CoreProject project);

        public IEnumerable<string> GetRecentProjects();
    }
}
