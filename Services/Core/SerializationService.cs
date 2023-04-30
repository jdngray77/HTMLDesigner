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
        SerializeModelToDisk<T>(model, uri);
    }
    
    public void SerializeModelToDisk<T>(T model, Uri uri)
    {
        XmlSerializer serializer = new XmlSerializer(typeof(T));
        TextWriter writer = new StreamWriter(uri.ToString());
        serializer.Serialize(writer, model);
    }
}