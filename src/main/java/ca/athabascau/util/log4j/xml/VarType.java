package ca.athabascau.util.log4j.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * <p>
 * Created :  2018-06-05T16:49 MST
 *
 * @author trenta
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class VarType
{ // BEGIN VariableType class
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String value;

    public String getValue()
    {
        return value;
    }

    public void setValue(final String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }
} // END VariableType class
