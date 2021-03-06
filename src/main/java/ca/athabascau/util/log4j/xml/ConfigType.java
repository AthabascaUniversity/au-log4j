//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.22 at 09:30:06 AM MDT 
//


package ca.athabascau.util.log4j.xml;

import org.apache.commons.lang3.text.StrSubstitutor;
import org.apache.log4j.helpers.LogLog;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * <p>Java class for configType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="configType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="filters" type="{}filterType" maxOccurs="unbounded"
 * minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@SuppressWarnings("PublicMethodNotExposedInInterface")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configType", propOrder = {"filters", "vars"})
@XmlRootElement(name = "config")
public class ConfigType
{
    @XmlElement(name = "filter")
    protected List<FilterType> filters;
    @XmlElement(name = "var")
    private List<VarType> vars;

    @XmlTransient
    private StrSubstitutor sub;

    /**
     * @param filterConfig
     *
     * @return the ConfigType instance with all appropriate options set.
     */
    public static ConfigType load(final String filterConfig)
    {
        InputStream configStream = ConfigType.class.getResourceAsStream(
            filterConfig);
        try
        {
            if (configStream == null)
            {
                configStream = new FileInputStream(filterConfig);
            }

            // config file exists
            final JAXBContext jaxbContext = JAXBContext.newInstance(
                ConfigType.class);
            final Unmarshaller marshaller = jaxbContext.createUnmarshaller();
            return (ConfigType) marshaller.unmarshal(configStream);
        }
        catch (final JAXBException e)
        {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        catch (final FileNotFoundException e)
        {
            LogLog.warn("filter-config.xml not present in the " +
                "classpath, ignoring!!! If you want to use filters with " +
                "ca.athabascau.util.log4j.SMTPAppender, then this " +
                "config file should be present");
        }

        return null;
    }

    /**
     * Gets the value of the filters property.
     * <p/>
     * <p/>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the filters property.
     * <p/>
     * <p/>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFilters().add(newItem);
     * </pre>
     * <p/>
     * <p/>
     * <p/>
     * Objects of the following type(s) are allowed in the list {@link
     * FilterType }
     */
    public List<FilterType> getFilters()
    {
        if (filters == null)
        {
            filters = new ArrayList<FilterType>();
        }

        for (final FilterType filterType : filters)
        {
            filterType.setParent(this);
        }

        return Collections.unmodifiableList(this.filters);
    }

    /**
     * Returns a matching filters.  The result may be null if nothing matched.
     *
     * @param logEntry the log entry to match.
     *
     * @return the matching filters or null if nothing matched.
     */
    public FilterType findMatch(final String logEntry)
    {
        getFilters();   // ensure filters is initialized
        final Optional<FilterType> result = filters
            .stream()
            .filter(filterType -> filterType.match(logEntry))
            .findFirst();
        return result.orElse(null);
    }

    public List<VarType> getVars()
    {
        if (vars == null)
        {
            vars = new ArrayList<>();
        }
        return vars;
    }

    public StrSubstitutor getSubstitutor()
    {
        if (this.sub == null)
        {
            final Map<String, String> substitutions = vars
                .stream()
                .collect(Collectors.toMap(VarType::getName, VarType::getValue));
            this.sub = new StrSubstitutor(substitutions);
        }
        return this.sub;
    }
}
