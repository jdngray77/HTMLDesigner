using HTML_Designer_MAUI.ViewModels.Core.Project;

namespace HTML_Designer_MAUI.Views.Core.Project;

public partial class NewProject : ContentPage
{
	public NewProject(NewProjectViewModel vm)
	{
		InitializeComponent();
		BindingContext = vm;
	}
}