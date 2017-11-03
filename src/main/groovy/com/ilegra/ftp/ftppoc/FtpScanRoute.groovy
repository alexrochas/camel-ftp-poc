package com.ilegra.ftp.ftppoc

import org.apache.camel.Exchange
import org.apache.camel.builder.RouteBuilder
import org.springframework.stereotype.Component

@Component
class FtpScanRoute extends RouteBuilder {
    @Override
    void configure() throws Exception {
        int loop = 0
        from("timer:producer?period=20000")
                .routeId("producer")
                .process({Exchange e ->
                    loop++
                    e.in.body = "Hello World"
                    e.in.setHeader(Exchange.FILE_NAME, "goForIt" + loop + ".dat")
                    e.in.setHeader('received_order', 'some json here')
                })
                .to("ftp://localhost:11021/Inbox?username=webservice" +
                    "&password=RAW(rif+pan)" +
                    "&passiveMode=true" +
                    "&tempPrefix=.tmp" +
                    "&binary=true")

        from("ftp://localhost:11021/Inbox?username=webservice" +
                    "&password=RAW(rif+pan)" +
                    "&passiveMode=true" +
                    "&binary=true" +
                    "&pollingConsumerQueueSize=50" +
                    "&delay=10s" +
                    "&preMove=.stage" +
                    "&include=.*\\.dat" +
                    "&move=../../Archive" +
                    "&moveFailed=../../Archive" +
                    "&maximumReconnectAttempts=0" +
                    "&reconnectDelay=10000")
                .routeId("consumerInbox")
                .process({Exchange e ->
                    System.out.println("From Inbox ${e.in.getBody(String)}")
                })

        from("ftp://localhost:11021/Outbox?username=webservice" +
                    "&password=RAW(rif+pan)" +
                    "&passiveMode=true" +
                    "&binary=true" +
                    "&pollingConsumerQueueSize=1" +
                    "&delay=10s" +
                    "&preMove=.stage" +
                    "&include=.*\\.dat" +
                    "&readLock=changed" +
                    "&readLockMinAge=500" +
                    "&readLockTimeout=4000" +
                    "&readLockCheckInterval=1000" +
                    "&readLockLoggingLevel=TRACE" +
                    "&ftpClientConfig.serverTimeZoneId=GMT" +
                    "&move=../../Archive" +
                    "&moveFailed=../../Archive" +
                    "&maximumReconnectAttempts=0" +
                    "&reconnectDelay=10000")
                .routeId("consumerOutbox")
                .process({Exchange e ->
                    System.out.println("From Outbox ${e.in.getBody(String)}")
                })
    }
}
