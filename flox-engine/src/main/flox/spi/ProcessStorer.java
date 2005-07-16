package flox.spi;

public interface ProcessStorer
{
    void save(Object context, String name, String definitionXml) throws ProcessStorerException;
}
