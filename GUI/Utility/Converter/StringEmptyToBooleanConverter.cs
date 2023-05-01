using System.Globalization;

namespace HTML_Designer_MAUI.Utility.Converter
{
    internal class StringEmptyToBooleanConverter : IValueConverter
    {
        public object Convert(object value, Type targetType, object parameter, CultureInfo culture)
        {
            return !string.IsNullOrEmpty(value as string);
        }

        public object ConvertBack(object value, Type targetType, object parameter, CultureInfo culture)
        {
            return value is false ? null : Binding.DoNothing;
        }
    }
}
