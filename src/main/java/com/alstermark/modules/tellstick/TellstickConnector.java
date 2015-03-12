/**
 * (c) 2003-2015 MuleSoft, Inc. The software in this package is published under the terms of the CPAL v1.0 license,
 * a copy of which has been included with this distribution in the LICENSE.md file.
 */

package com.alstermark.modules.tellstick;

import org.mule.api.annotations.ConnectionStrategy;
import org.mule.api.annotations.Connector;
import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.Processor;
import org.mule.api.annotations.param.Default;

import com.alstermark.modules.tellstick.strategy.ConnectorConnectionStrategy;

/**
 * Anypoint Connector
 *
 * @author MuleSoft, Inc.
 */
@Connector(name="tellstick", friendlyName="Tellstick", schemaVersion="current")
public class TellstickConnector {
    /**
     * Configurable
     */
    @Configurable
    @Default("value")
    private String myProperty;

    @ConnectionStrategy
    ConnectorConnectionStrategy connectionStrategy;

    /**
     * Custom processor
     *
     * {@sample.xml ../../../doc/tellstick-connector.xml.sample tellstick:my-processor}
     *
     * @param content Content to be processed
     * @return Some string
     */
    @Processor
    public String myProcessor(String content) {
        /*
         * MESSAGE PROCESSOR CODE GOES HERE
         */
        return content;
    }

    /**
     * Set property
     *
     * @param myProperty My property
     */
    public void setMyProperty(String myProperty) {
        this.myProperty = myProperty;
    }

    /**
     * Get property
     */
    public String getMyProperty() {
        return this.myProperty;
    }

    public ConnectorConnectionStrategy getConnectionStrategy() {
        return connectionStrategy;
    }

    public void setConnectionStrategy(ConnectorConnectionStrategy connectionStrategy) {
        this.connectionStrategy = connectionStrategy;
    }

}