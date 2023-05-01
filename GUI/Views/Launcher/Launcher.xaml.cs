namespace HTML_Designer_MAUI.Views.Launcher;

using HTML_Designer_MAUI.ViewModels.Launcher;

public partial class Launcher : ContentPage
{
    public Launcher(LauncherViewModel vm)
    {
        InitializeComponent();
        BindingContext = vm;
    }
}