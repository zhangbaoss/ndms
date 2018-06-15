package nurteen.prometheus.pc.center.server.controller;

import nurteen.prometheus.pc.framework.Reason;
import nurteen.prometheus.pc.framework.utils.TimeUtils;
import nurteen.prometheus.pc.framework.web.socket.*;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsController;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnMessage;

import java.util.List;

@WsController
public class WsAppMobileController {

    @WsOnMessage(url = "/test/gggggg")
    public void testgggggg(WsMessage msg) {
        System.out.println("onMessage: " + msg.getPayload());
        msg.response("dddddddd");
    }
    @WsOnMessage(url = "/test/gggggg/g")
    public void testggggggg(WsMessage msg) {
        System.out.println("onMessage: " + msg.getPayload());
        /*
        WsMessageDispatcher.request("/uuuuuuuuuuuuuuuu", "中文", new WsRouteResponse() {
                    @Override
                    public void result(String ndid, String url, boolean succes, String message) {
                        System.out.println("WsRouteResponse" + message);
                    }
                },
                new WsResponse<String>() {
                    @Override
                    public void reply(WsMessage<String> msg) {
                        System.out.println("WsResponse" + msg.getPayload());
                    }
                });
                */
        // return "test";
    }

    @WsOnMessage(url = "/uuuuuuuuuuuuuuuu/hhhhhhhhhhhhhhhhhhh")
    public void uuuuuuuuuuuuuuuu(WsMessage msg) {
        System.out.println("server capture message: " + msg.getPayload());

        long t = TimeUtils.currentTimeNsecs();
        long tt = System.currentTimeMillis() * 1000000L;

        WsMessageDispatcher.request(msg, "server processed", 10000, new WsResponse() {
            @Override
            public void resolve(List<String> routes) {
                System.out.println("request message arrive target. url = " + msg.getUrl());
                for (String route : routes) {
                    System.out.println("route = " + route);
                }
            }

            @Override
            public void resolve(WsMessage message) {
                System.out.println("server get response message: " + msg.getPayload());
                msg.response(message.getPayload());
            }

            @Override
            public void reject(List<String> routes, Reason reason) {
                System.out.println("request message not arrive target. url = " + msg.getUrl() + ", reason: " + reason.getMessage());
                for (String route : routes) {
                    System.out.println("route = " + route);
                }
            }
        });
    }
}
