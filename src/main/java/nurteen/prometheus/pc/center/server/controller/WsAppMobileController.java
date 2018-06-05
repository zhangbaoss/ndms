package nurteen.prometheus.pc.center.server.controller;

import nurteen.prometheus.pc.framework.authentication.entities.LoginArgs;
import nurteen.prometheus.pc.framework.utils.WsUtils;
import nurteen.prometheus.pc.framework.web.socket.WsMsg;
import nurteen.prometheus.pc.framework.web.socket.WsServer;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsController;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnClose;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnMessage;
import nurteen.prometheus.pc.framework.web.socket.annotation.WsOnOpen;

@WsController
public class WsAppMobileController {

    @WsOnOpen
    public void onOpen(WsServer wsServer) {
        System.out.println("onOpen");
    }
    @WsOnClose
    public void onClose(WsServer wsServer) {
        System.out.println("onClose");
    }
    @WsOnMessage(url = "/test/gggggg")
    public String testgggggg(WsMsg<String> msg) {
        System.out.println("onMessage: " + msg.getPayload());
        return "test";
    }
    @WsOnMessage(url = "/test/gggggg/g")
    public void testggggggg(WsMsg<LoginArgs> msg) {
        System.out.println("onMessage: " + msg.getPayload().getAccount());
        WsUtils.request("/uuuuuuuuuuuuuuuu", "中文");
        // return "test";
    }
    @WsOnMessage(url = "/uuuuuuuuuuuuuuuu")
    public void uuuuuuuuuuuuuuuu() {
        System.out.println("uuuuuuuuuuuuuuuu");
    }
}
