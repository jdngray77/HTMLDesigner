using Utility.Core.Project;

namespace Services.Interfaces;

public interface ISerializationService
{
    T DeserializeModelFromDisk<T>(Uri uri);
    T DeserializeModel<T>(string model);
    string SerializeModel<T>(T model);
    void SerializeModelToDisk<T>(T model, Uri uri);
    void SerializeModelToDisk<T>(T model, ProjectPath uri);
}