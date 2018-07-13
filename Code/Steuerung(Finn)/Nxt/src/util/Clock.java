package util;

import datatypes.TimedListener;

public class Clock {

	private static TimedListener[] listeners = new TimedListener[] {};

	public static void addTimer(TimedListener l) {
		if (listeners.length == 0) {
			startClock();

		}
		TimedListener[] lst = listeners;
		listeners = new TimedListener[listeners.length + 1];
		for (int i = 0; i < lst.length; i++) {
			listeners[i] = lst[i];
		}
		listeners[listeners.length - 1] = l;
	}

	private static void startClock() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				for (TimedListener l : listeners) {
					System.out.println(listeners.length);
					l.TimeUp();
				}

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
