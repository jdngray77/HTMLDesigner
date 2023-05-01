using CommunityToolkit.Mvvm.ComponentModel;
using CommunityToolkit.Mvvm.Input;
using System.Diagnostics;
using Utility.Core.Project;
using Utility.Core;
using System.Reflection;
using HTML_Designer_MAUI.Views.Launcher;

namespace HTML_Designer_MAUI.ViewModels.Launcher
{
    public partial class LauncherViewModel : ObservableObject
    {
        [ObservableProperty]
        public List<Project> projects;

        [ObservableProperty]
        public Project? selectedProject;

        public LauncherViewModel()
        {
            projects = new List<Project>()
            {
                new Project("Test", "Test", new Uri("C:\\Users\\james\\Desktop\\TestC:\\Users\\james\\Desktop\\TestC:\\Users\\james\\Desktop\\TestC:\\Users\\james\\Desktop\\Test")),
                new Project("Test2", "Test2", new Uri("C:\\Users\\james\\Desktop\\Test2C:\\Users\\james\\Desktop\\Test")),
                new Project("Test3", "Test3", new Uri("C:\\Users\\james\\Desktop\\Test3C:\\Users\\james\\Desktop\\TestC:\\Users\\james\\Desktop\\TestC:\\Users\\james\\Desktop\\Test")),
                new Project("Test4", "Test4", new Uri("C:\\Users\\jordan\\Desktop\\")),
            };
        }

        [RelayCommand]
        public void Exit()
        {
            Environment.Exit((int)ExitCodes.UserExitRequest);
        }

        [RelayCommand]
        public void NewProject()
        {
            Shell.Current.GoToAsync(nameof(NewProject));
        }

        [RelayCommand(CanExecute = nameof(CanOpenProject))]
        public void OpenProject()
        {
            Shell.Current.GoToAsync(nameof(Editor));
        }

        [RelayCommand]
        public void OpenProjectFolder()
        {
            if (selectedProject is not null)
            {
                Microsoft.Maui.ApplicationModel.Launcher.Default.OpenAsync(SelectedProject.Path);
            }
        }   

        private bool CanOpenProject()
        {
            return selectedProject is not null;
        }
    }
}
