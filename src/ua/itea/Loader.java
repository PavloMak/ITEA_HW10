package ua.itea;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class Loader implements Runnable {

	private final int LOAD_PER_SECOND = 2;
	private int gold = 100;
	private Thread thread;
	private Transporter transporter = null;
	private CountDownLatch loadingLatch;

	public Loader(CountDownLatch loadingLatch) {
		this.loadingLatch = loadingLatch;
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
		while (gold > 0) {
			System.out.println("Started loading");

			for (int i = 0; i < 3; i++) {
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if (gold - LOAD_PER_SECOND < 0) {
					transporter.loadCargo(gold);
					gold -= gold;
				} else {
					transporter.loadCargo(LOAD_PER_SECOND);
					gold -= LOAD_PER_SECOND;
				}
				loadingLatch.countDown();
			}
			System.out.println("Finished loading");
		
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}	
		System.out.println("No more gold");
		transporter.endOfWork();
	}

	public void setTransporter(Transporter transporter) {
		this.transporter = transporter;
	}

	public synchronized void resume() {
		notify();
	}

}
