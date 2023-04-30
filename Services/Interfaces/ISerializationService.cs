using Utility.Core.Project;

namespace Services.Interfaces;

public interface ISerializationService
{
    string SerializeModel<T>(T model);
    void SerializeModelToDisk<T>(T model, Uri uri);
    void SerializeModelToDisk<T>(T model, ProjectPath uri);
}