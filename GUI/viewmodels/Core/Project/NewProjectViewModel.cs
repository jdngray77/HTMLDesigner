using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using Services.Interfaces.Project;
using CoreProject = Utility.Core.Project.Project;

namespace HTML_Designer_MAUI.ViewModels.Core.Project
{
    public partial class NewProjectViewModel : ObservableObject
    {
        private IProjectManagementService projectManager;
        
        public NewProjectViewModel(IProjectManagementService projectManager)
        {
            this.projectManager = projectManager;
        }

        [ObservableProperty]
        public string displayName = string.Empty;

        [ObservableProperty]
        public string description = string.Empty;

        [ObservableProperty]
        public string author = string.Empty;

        [ObservableProperty]
        public string tags = string.Empty;

        [ObservableProperty]
        public string version = "1";

        [RelayCommand]
        public void Cancel()
        {
            Shell.Current.GoToAsync("..");
        }

        [RelayCommand]
        public void Create()
        {
            // TODO : Add validation
            CoreProject p = new CoreProject(DisplayName, Description, Author, Tags.Split(", "), Double.Parse(Version), new Uri("C:\\\\Users\\jordan\\Desktop\\test\\"));
            try
            {
                projectManager.CreateNewProject(p);
            } catch (Exception e)
            {
                return;
            }

            Shell.Current.GoToAsync("..");
        }
    }
}
