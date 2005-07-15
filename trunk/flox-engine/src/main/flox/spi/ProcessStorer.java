package flox.spi;

import flox.def.Process;


public interface ProcessStorer
{
    void save(Object context, String name, String definitionXml) throws ProcessStorerException;
}
