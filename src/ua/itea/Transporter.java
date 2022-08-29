package ua.itea;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Transporter implements Runnable {

	private int cargo;
	private boolean doHaveWork = true;
	private Thread thread;
	private Loader loader;
	private Unloader unloader;
	private CountDownLatch loadingLatch;
	private CountDownLatch unloadingLatch;

	public Transporter(Loader loader, Unloader unloader, CountDownLatch loadingLatch, CountDownLatch unloadingLatch) {
		this.loader = loader;
		this.unloader = unloader;
		this.loadingLatch = loadingLatch;
		this.unloadingLatch = unloadingLatch;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		loader.setTransporter(this);
		unloader.setTransporter(this);
		while (doHaveWork) {
			loader.resume();
			try {
				loadingLatch.await();
				System.out.println("Transporting");
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			unloader.resume();
			try {
				unloadingLatch.await();
				System.out.println("Transporting");
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
		System.out.println("Now we all can rest");
	}

	public void loadCargo(int amount) {
		cargo += amount;
	}

	public int unloadCargo(int amount) {
		int toUnload;
		if (cargo - amount < 0) {
			toUnload = cargo;
		} else {
			toUnload = amount;
			cargo -= amount;
		}
		return toUnload;
	}

	public void endOfWork() {
		doHaveWork = false;
	}

}
