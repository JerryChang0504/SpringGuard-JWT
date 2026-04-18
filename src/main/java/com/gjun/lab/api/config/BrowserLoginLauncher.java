package com.gjun.lab.api.config;

import java.awt.Desktop;
import java.awt.GraphicsEnvironment;
import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 本機開發時於啟動完成後開啟首頁；無圖形介面或容器環境會自動略過。
 */
@Component
public class BrowserLoginLauncher implements ApplicationListener<ApplicationReadyEvent> {

	private static final Logger log = LoggerFactory.getLogger(BrowserLoginLauncher.class);

	@Value("${app.browser.open-login:true}")
	private boolean openLogin;

	@Value("${server.port:8080}")
	private int serverPort;

	@Value("${server.servlet.context-path:}")
	private String contextPath;

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		if (!openLogin) {
			return;
		}
		if (GraphicsEnvironment.isHeadless()) {
			log.debug("Headless environment：略過自動開啟瀏覽器。");
			return;
		}
		String base = normalizeContextPath(contextPath);
		String url = "http://127.0.0.1:" + serverPort + base + "/";
		try {
			if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
				Desktop.getDesktop().browse(URI.create(url));
				log.info("已嘗試開啟瀏覽器：{}", url);
			}
		}
		catch (Exception ex) {
			log.debug("無法開啟瀏覽器（{}）。請手動開啟：{}", ex.getMessage(), url);
		}
	}

	private static String normalizeContextPath(String path) {
		if (path == null || path.isBlank()) {
			return "";
		}
		String p = path.startsWith("/") ? path : "/" + path;
		return p.endsWith("/") ? p.substring(0, p.length() - 1) : p;
	}
}
