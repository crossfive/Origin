package com.crossfive.framework.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;

import com.crossfive.framework.common.init.ControllableService;
import com.crossfive.framework.common.init.Initializable;

public class CustomApplication implements DisposableBean, ControllableService {

	private static final Log logger = LogFactory.getLog(CustomApplication.class);

	private boolean isRunning = false;

	private Thread mainThread;

	private GenericApplicationContext context;

	private final String[] beanFiles;
	
	private Initializable initializer;

	public CustomApplication(Initializable initializer, String... beanFiles) {
		this.beanFiles = beanFiles;
		this.initializer = initializer;
	}

	@Override
	public boolean isRunning() {
		return isRunning;
	}

	@Override
	public void stop() {
		logger.info("[Secular System] Service shutdowning...");
		this.mainThread.interrupt();
	}

	@Override
	public boolean start() {
		try {
			logger.info("[Secular System] Service startup...");
			mainThread = Thread.currentThread();
			context = new GenericApplicationContext();
			ApplicationContext ac = null;
			try {
				// 优先尝试类路径加载
				ac = new ClassPathXmlApplicationContext(beanFiles);
			}catch (BeansException be) {
				// 如果类路径加载失败，兼容使用文件路径的方式
				ac = new FileSystemXmlApplicationContext(beanFiles);
			}
			context.setParent(ac);

			AnnotationConfigUtils.registerAnnotationConfigProcessors(context);
			context.refresh();

			context.registerShutdownHook();
			context.start();
			
			initializer.setContext(context);
			initializer.init();
			logger.info("[Secular System] Service started..");

			while (!Thread.interrupted()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.warn("[Secular System] Server stopping...");
					break;
				}
			}

			this.context.stop();
			this.context.close();
			logger.warn("[Secular System] Server stopped");
			System.exit(0);
		} catch (Exception e) {
			logger.error(this + " start up error:", e);
			return false;
		}
		return true;
	}

	@Override
	public void destroy() throws Exception {
		this.stop();
	}

	public GenericApplicationContext getContext() {
		return context;
	}

}
