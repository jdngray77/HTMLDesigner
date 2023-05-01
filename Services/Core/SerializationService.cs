using System.Xml.Serialization;
using Services.Interfaces;
using Utility.Core.Project;

namespace Services.Core;

public class SerializationService : ISerializationService
{
    public string SerializeModel<T>(T model)
    {
        XmlSerializer serializer = new XmlSerializer(typeof(T));
        TextWriter writer = new StringWriter();
        serializer.Serialize(writer, model);
        return writer.ToString();
    }

    public void SerializeModelToDisk<T>(T model, ProjectPath uri)
    {
        SerializeModelToDisk<T>(model, (Uri)uri);
    }
    
    public void SerializeModelToDisk<T>(T model, Uri uri)
    {
        XmlSerializer serializer = new XmlSerializer(typeof(T));
        using TextWriter writer = new StreamWriter(uri.LocalPath);
        serializer.Serialize(writer, model);
    }

    public T DeserializeModelFromDisk<T>(Uri uri)
    {
        XmlSerializer serializer = new XmlSerializer(typeof(T));
        using TextReader reader = new StreamReader(uri.ToString());
        return (T)serializer.Deserialize(reader);
    }
}