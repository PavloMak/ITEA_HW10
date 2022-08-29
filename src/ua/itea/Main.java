package ua.itea;

import java.util.concurrent.CountDownLatch;

public class Main {

	public static void main(String[] args) {
		CountDownLatch loadingLatch = new CountDownLatch(3);
		CountDownLatch unloadingLatch = new CountDownLatch(2);
		Loader loader = new Loader(loadingLatch);
		Unloader unloader = new Unloader(unloadingLatch);
		Transporter transporter = new Transporter(loader, unloader, loadingLatch, unloadingLatch);

	}

}
