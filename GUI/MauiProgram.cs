using Microsoft.Extensions.Logging;
using Services.Core;
using Services.Interfaces;
using Launcher = HTML_Designer_MAUI.Views.Launcher.Launcher;

namespace HTML_Designer_MAUI;

public static class MauiProgram
{
	public static MauiApp CreateMauiApp()
	{
		var builder = MauiApp.CreateBuilder();
		builder
			.UseMauiApp<App>()
			.ConfigureFonts(fonts =>
			{
				fonts.AddFont("OpenSans-Regular.ttf", "OpenSansRegular");
				fonts.AddFont("OpenSans-Semibold.ttf", "OpenSansSemibold");
			});

#if DEBUG
		builder.Logging.AddDebug();
#endif

		return builder.Build();
	}

	private void ConfigureViews(MauiAppBuilder builder)
	{
		builder.Services.AddTransient<Launcher>();
	}

	private void RegisterViewModels()
	{
	}

	private void RegisterRoutes()
	{
		Routing.RegisterRoute(Routes.Launcher, typeof(Launcher));
	}

	private void RegisterServices(MauiAppBuilder builder)
	{
		builder.Services.AddSingleton<ISerializationService, SerializationService>();
	}
}
