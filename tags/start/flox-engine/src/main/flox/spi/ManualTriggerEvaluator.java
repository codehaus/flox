package flox.spi;

import flox.def.ManualTriggerDefinition;

/**
 * Created by IntelliJ IDEA.
 * User: bob
 * Date: Mar 18, 2005
 * Time: 3:45:18 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ManualTriggerEvaluator
{
    public boolean evaluate(ManualTriggerDefinition def);
}
