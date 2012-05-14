package net.cloudsom.cloudsql;

import org.eclipse.jetty.server.Server;
import org.springside.modules.test.functional.JettyFactory;

/**
 * 使用Jetty运行调试Web应用, 在Console输入回车停止服务器.
 * 
 * @author zhulin
 */
public class Start {

	public static final int PORT = 8080;
	public static final String CONTEXT = "/cloudsql";
	public static final String BASE_URL = "http://localhost:8080/cloudsql";

	public static void main(String[] args) throws Exception {
		Server server = JettyFactory.buildNormalServer(PORT, CONTEXT);
		server.start();
		System.out.println("Server running at " + BASE_URL);
		System.out.println("Hit Enter in console to stop server");
		if (System.in.read() != 0) {
			server.stop();
			System.out.println("Server stopped");
		}
	}
}
