package skyler.tao.simple;

public class SimpleThreads {

	static void threadMessage (String message) {
		String threadName = Thread.currentThread().getName();
		System.out.format("%s: %s%n", threadName, message);
	}
	
	private static class MessageLoop implements Runnable {
		public void run() {
			String[] importInfo = {"message1", "message2", "message3", "message4", "message5"};
			
			try {
				for (int i = 0; i < importInfo.length; i++) {
					Thread.sleep(4000);
					threadMessage(importInfo[i]);
				}
			} catch (InterruptedException e) {
				threadMessage("I wasn't done!");
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
//		long patience = 1000 * 60 * 60;	//default: one hour
		long patience = 8000;
		//if command line argument present, gives patience in seconds.
		if (args.length > 0) {
			try {
				patience = Long.parseLong(args[0]) * 1000;
			} catch (NumberFormatException e) {
				System.err.println("Argument must be an integer!");
				System.exit(1);
			}
		}
		
		threadMessage("Starting messageLoop thread.");
		long startTime = System.currentTimeMillis();
		Thread t = new Thread(new MessageLoop());
		t.start();
		
		threadMessage("Waiting for messageLoop thread to finish.");
		
		while (t.isAlive()) {
			threadMessage("Still waiting ...");
			t.join(1000);
			if (((System.currentTimeMillis() - startTime) > patience) && t.isAlive()) {
				threadMessage("Tired of waiting!");
				t.interrupt();
				t.join();
			}
		}
		threadMessage("Finally!");
	}
}
