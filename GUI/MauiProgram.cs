using HTML_Designer_MAUI.ViewModels.Core.Project;
using HTML_Designer_MAUI.ViewModels.Launcher;
using HTML_Designer_MAUI.Views.Core.Project;
using Microsoft.Extensions.Logging;
using Services.Core;
using Services.Interfaces;
using Launcher = HTML_Designer_MAUI.Views.Launcher.Launcher;
using CommunityToolkit.Maui;
using Services.Interfaces.Project;
using HTML_Designer_MAUI.Utility;

namespace HTML_Designer_MAUI;

public static class MauiProgram
{
	public static MauiApp CreateMauiApp()
	{
		var builder = MauiApp.CreateBuilder();
		builder
			.UseMauiApp<App>()
			.UseMauiCommunityToolkit()
			.ConfigureFonts(fonts =>
			{
				fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
				fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
			});

#if DEBUG
		builder.Logging.AddDebug();
#endif

		ConfigureViews(builder);
		RegisterViewModels(builder);
		RegisterServices(builder);
        RegisterRoutes();

        return builder.Build();
	}

	private static void ConfigureViews(MauiAppBuilder builder)
	{
		builder.Services.AddTransient<Launcher>();
		builder.Services.AddTransient<NewProject>();
	}

	private static void RegisterViewModels(MauiAppBuilder builder)
    {
		builder.Services.AddTransient<LauncherViewModel>();
		builder.Services.AddTransient<NewProjectViewModel>();
	}

	private static void RegisterRoutes()
	{
		Routing.RegisterRoute(nameof(Launcher), typeof(Launcher));
		Routing.RegisterRoute(nameof(NewProject), typeof(NewProject));
	}

    private static void RegisterServices(MauiAppBuilder builder)
	{
		builder.Services.AddSingleton<ISerializationService, SerializationService>();
		builder.Services.AddSingleton<IProjectManagementService, ProjectManagementService>();
		builder.Services.AddSingleton<IPreferencesService, MauiPreferencesService>();
	}
}
