package com.example.co_reading.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	private ExecutorService service;
	private static ThreadPool self;
	private final static int threadNum = 5;
	
	private ThreadPool(int num) {
		service = Executors.newFixedThreadPool(num);
	}
	
	public static ExecutorService getService() {
		if (self == null)
			self = new ThreadPool(threadNum);
		return self.service;
	}
}
