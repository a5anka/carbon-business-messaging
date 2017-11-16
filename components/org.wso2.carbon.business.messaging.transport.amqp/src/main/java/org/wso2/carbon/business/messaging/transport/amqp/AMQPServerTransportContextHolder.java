package org.wso2.carbon.business.messaging.transport.amqp;

import org.wso2.carbon.business.messaging.core.service.BrokerService;

/**
 * Keep all the service reference objects required for component
 */
public class AMQPServerTransportContextHolder {
    private static AMQPServerTransportContextHolder instance = new AMQPServerTransportContextHolder();
    private BrokerService brokerService;

    public static AMQPServerTransportContextHolder getInstance() {
        return instance;
    }

    public void setBrokerService(BrokerService brokerService) {
        this.brokerService = brokerService;
    }

    public BrokerService getBrokerService() {
        return brokerService;
    }
}
