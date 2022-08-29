package ua.itea;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Unloader implements Runnable {

	private final int UNLOAD_PER_SECOND = 3;
	private int gold = 0;
	private Thread thread;
	private Transporter transporter = null;
	private CountDownLatch unloadingLatch;

	public Unloader(CountDownLatch unloadingLatch) {
		this.unloadingLatch = unloadingLatch;
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		synchronized (this) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		while (true) {
			System.out.println("Started unloading");
			for (int i = 0; i < 2; i++) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				gold += transporter.unloadCargo(UNLOAD_PER_SECOND);
				unloadingLatch.countDown();
			}
			System.out.println("Finished unloading");
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public synchronized void resume() {
		notify();
	}

}
