package util;

import datatypes.NXTMessage;
import lejos.nxt.LCD;
import lejos.nxt.comm.NXTConnection;
import lejos.nxt.comm.USB;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * the Connection class
 */
public class Connection {

	public static Boolean connected;
	private static NXTConnection usb;
	private static DataOutputStream out;
	private static DataInputStream in;

	/**
	 * this method waits for a USB connection
	 */
	public static void connect() {
		LCD.drawString("waiting for connection", 0, 0);
		while (usb == null) {
			usb = USB.waitForConnection();
		}
		out = usb.openDataOutputStream();
		in = usb.openDataInputStream();

		LCD.clear();
		LCD.drawString("Connected", 0, 0);

		connected = true;

		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				while (connected) {
					receive();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	private static synchronized void receive() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					receive(in.readLong());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	/**
	 * this method handles new Messages
	 */
	private static synchronized void receive(Long in) {
		try {
			if (in == -1) {
				disconnect();
			} else if (in == -2) {
				out.writeLong(-2);
				out.flush();
			} else if (in == -3) {
				out.writeLong(System.currentTimeMillis());
				out.flush();
			} else {
				Utilities.handleInput(false, NXTMessage.toNxtMessage(in));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method sends a NXTMessage to the pi
	 *
	 * @param nxtMessage the NXTMessage
	 */
	public static void send(NXTMessage nxtMessage) {
		try {
			// System.out.println(nxtMessage.toLong());
			out.writeLong(nxtMessage.toLong());
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * this method disconnects from the pi
	 */
	public static void disconnect() {
		try {
			out.writeLong(-1);
			out.flush();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		connected = false;

		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		usb.close();
	}
}
