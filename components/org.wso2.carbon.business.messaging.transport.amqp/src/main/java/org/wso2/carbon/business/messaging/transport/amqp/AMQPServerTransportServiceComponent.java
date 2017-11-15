package org.wso2.carbon.business.messaging.transport.amqp;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to manage broker AMPQ transport
 */
@Component(name = "org.wso2.carbon.business.messaging.transport.amqp.AMQPServerTransportServiceComponent",
           immediate = true,
           property = {
                   "componentName=broker-amqp-transports-mgt"
           })
@SuppressWarnings("unused")
public class AMQPServerTransportServiceComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AMQPServerTransportServiceComponent.class);

    @Activate
    protected void activate(BundleContext bundleContext) {
        LOGGER.info("broker-amqp-transports-mgt component activated activated");
    }
}
