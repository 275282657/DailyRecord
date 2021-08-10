package com.cgp.dailyrecord.nio.sever;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
	public static void main(String[] args) {
		Server();
	}
	private static ExecutorService executorService = new ThreadPoolExecutor(3, 5, 3,
																			TimeUnit.MINUTES, new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());

	public static void Server(){
		ServerSocketChannel serverSocketChannel = null;
		Selector selector = null;
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(8888));
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				// select方法指定超时时间，非阻塞
				if (selector.select(2000) == 0) {
					System.out.println("*");
				}

				Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
				while (iterator.hasNext()){
					SelectionKey selectionKey = iterator.next();
					//如果服务端信道感兴趣的I/O操作为accept
					if (selectionKey.isAcceptable()){
						SocketChannel socketChannel = serverSocketChannel.accept();
						socketChannel.configureBlocking(false);
						socketChannel.register(selector, SelectionKey.OP_READ);
					}
					//如果客户端信道感兴趣的I/O操作为read
					if (selectionKey.isReadable()){
						// 针对每次read，使用线程池机制去处理
						executorService.submit(new MyRunnable(selectionKey));
					}
					//这里需要手动从键集中移除当前的key
					iterator.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				selector.close();
				serverSocketChannel.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	static class MyRunnable implements Runnable{

		private SelectionKey selectionKey;

		public MyRunnable(SelectionKey selectionKey){
			this.selectionKey = selectionKey;
		}

		@Override
		public void run() {
			SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int read = 0;
			try {
				read = socketChannel.read(buffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (read == -1){
				// 客户端关闭
				selectionKey.cancel();
			} else {
				// 对客户端信息处理
				System.out.println("客户端说：" + new String(buffer.array()));
			}
		}
	}
}


