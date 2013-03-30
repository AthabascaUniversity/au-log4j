/**
 * This file is part of the au-log4j package; aka Athabasca University log4j
 * addons.
 * 
 * Copyright Trenton D. Adams <trenton daught d daught adams at gmail daught ca>
 * 
 * au-log4j is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * au-log4j is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
 * License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public 
 * License along with au-log4j.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * See the COPYING file for more information.
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.06.22 at 09:30:06 AM MDT 
//


package ca.athabascau.util.log4j.xml;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Java class for filterType complex type.
 * <p/>
 * <p>The following schema fragment specifies the expected content contained
 * within this class.
 * <p/>
 * <pre>
 * &lt;complexType name="filterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="to" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="message" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="regex" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *       &lt;attribute name="log" type="{http://www.w3.org/2001/XMLSchema}boolean"
 * />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
/*@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "filterType", propOrder = {
    "to", "message", "regex"})*/
public class FilterType
{

    //    @XmlElement(required = true)
    protected String to;
    //    @XmlElement(required = true)
    protected String message;
    //    @XmlElement(required = true)
    protected String regex;
    //    @XmlAttribute
    protected Boolean log;

    /**
     * Gets the value of the to property. The email address to send the errors
     * which match this rule to.
     *
     * @return possible object is {@link String }
     */
    public String getTo()
    {
        return to;
    }

    /**
     * Sets the value of the to property.
     *
     * @param value allowed object is {@link String }
     *
     * @see #getTo()
     */
    public void setTo(final String value)
    {
        this.to = value;
    }

    /**
     * Gets the message that should be display with the log entry.
     *
     * @return possible object is {@link String }
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the value of the message property.
     *
     * @param value allowed object is {@link String }
     *
     * @see #getMessage()
     */
    public void setMessage(final String value)
    {
        this.message = value;
    }

    /**
     * The regular expression to match the message
     *
     * @return possible object is {@link String }
     */
    public String getRegex()
    {
        return regex;
    }

    /**
     * Sets the value of the regex property.
     *
     * @param value allowed object is {@link String }
     *
     * @see #getRegex()
     */
    public void setRegex(final String value)
    {
        this.regex = value;
    }

    /**
     * Is this something that should be logged by email?
     *
     * @return possible object is {@link Boolean }
     */
    public Boolean isLog()
    {
        return log;
    }

    /**
     * Sets the value of the log property.
     *
     * @param value allowed object is {@link Boolean }
     *
     * @see #isLog()
     */
    public void setLog(final Boolean value)
    {
        this.log = value;
    }


    public static FilterType load(final Node node)
    {
        final FilterType filter = new FilterType();
        final NamedNodeMap attributes = node.getAttributes();
        if (attributes.getLength() != 0)
        {
            final Node log = attributes.getNamedItem("log");
            if (log != null)
            {
                filter.setLog(Boolean.valueOf(log.getNodeValue()));
            }
        }
        final NodeList children = node.getChildNodes();
        for (int index = 0; index < children.getLength(); index++)
        {
            final Node childNode = children.item(index);
            if ("to".equals(childNode.getNodeName()))
            {
                filter.setTo(getText(childNode));
            }
            else if ("message".equals(childNode.getNodeName()))
            {
                filter.setMessage(getText(childNode));
            }
            else if ("regex".equals(childNode.getNodeName()))
            {
                filter.setRegex(getText(childNode));
            }
        }

        if (filter.getMessage() == null ||
            filter.getTo() == null && filter.isLog().booleanValue() ||
            filter.getRegex() == null)
        {
            throw new IllegalArgumentException("One of message, to, or " +
                "regex elements is missing in the filters-config.xml");
        }

        return filter;
    }

    private static String getText(final Node node)
    {
        if (node.getFirstChild() == null ||
            node.getFirstChild().getNodeType() != Node.TEXT_NODE ||
            node.getFirstChild().getNodeValue() == null)
        {
            return "";
        }
        else
        {
            return node.getFirstChild().getNodeValue();
        }
    }

    /**
     * Checks to see if this filter matches the log entry.
     *
     * @param logEntry the log entry to match against.
     *
     * @return true if matched.
     */
    public boolean match(final String logEntry)
    {
        return logEntry.matches(regex);
    }
}
