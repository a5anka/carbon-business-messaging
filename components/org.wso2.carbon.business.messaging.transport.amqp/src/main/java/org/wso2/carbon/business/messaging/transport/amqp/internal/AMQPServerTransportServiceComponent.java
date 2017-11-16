package org.wso2.carbon.business.messaging.transport.amqp.internal;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.wso2.carbon.business.messaging.core.service.BrokerService;
import org.wso2.carbon.business.messaging.transport.amqp.internal.netty.AmqpTransport;

/**
 * Component used to manage broker AMPQ transport
 */
@Component(name = "org.wso2.carbon.business.messaging.transport.amqp.internal.AMQPServerTransportServiceComponent",
           immediate = true,
           property = {
                   "componentName=broker-amqp-transports-mgt"
           })
@SuppressWarnings("unused")
public class AMQPServerTransportServiceComponent {
    private static final Logger LOGGER = LoggerFactory.getLogger(AMQPServerTransportServiceComponent.class);

    /**
     * Used to manage AMQP transport
     */
    private AmqpTransport transport;

    @Activate
    protected void activate(BundleContext bundleContext) {
        transport = new AmqpTransport();
        transport.start();

        LOGGER.info("AMQP transport initialized");
    }

    @Deactivate
    protected void deactivate(BundleContext bundleContext) {
        transport.stop();

        LOGGER.info("AMQP transport shutdown");
    }

    /**
     * This method will be called to add/register the broker service with the broker
     *
     * @param brokerService the declarative service which allows access to db instance
     */
    @Reference(name = "org.wso2.carbon.business.messaging.core.service.BrokerService",
               service = BrokerService.class,
               cardinality = ReferenceCardinality.MANDATORY,
               policy = ReferencePolicy.DYNAMIC,
               unbind = "unregisterBrokerService")
    protected void registerBrokerService(BrokerService brokerService) {
        AMQPServerTransportContextHolder.getInstance().setBrokerService(brokerService);
    }

    /**
     * Unregister broker service
     *
     * @param brokerService broker service description
     */
    protected void unregisterBrokerService(BrokerService brokerService) {
        AMQPServerTransportContextHolder.getInstance().setBrokerService(null);
    }
}
