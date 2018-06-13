package nurteen.prometheus.pc.center.server.controller;

import nurteen.prometheus.pc.framework.web.socket.*;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsController;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnMessage;

@WsController
public class WsAppMobileController {

    @WsOnMessage(url = "/test/gggggg")
    public String testgggggg(WsMessage msg) {
        System.out.println("onMessage: " + msg.getPayload());
        return "test";
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
    /*
    @WsOnMessage(url = "/uuuuuuuuuuuuuuuu/hhhhhhhhhhhhhhhhhhh")
    public void uuuuuuuuuuuuuuuu(WsMessage msg) {
        System.out.println("uuuuuuuuuuuuuuuu");
    }
    */
}
