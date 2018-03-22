package io.tokhn.view;

import java.util.concurrent.ConcurrentLinkedQueue;

import io.grpc.stub.StreamObserver;
import io.tokhn.Main;
import io.tokhn.grpc.BlockModel;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class DigitalRain extends Canvas {
	private final static int FONT_SIZE = 16;
	private RainDrop[] drops;
	private Main main;
	private ConcurrentLinkedQueue<String> blockIds = new ConcurrentLinkedQueue<>();
	
	public DigitalRain() {
		// Redraw canvas when size changes.
		widthProperty().addListener(evt -> draw());
		heightProperty().addListener(evt -> draw());
		
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), event -> draw()), new KeyFrame(Duration.millis(20)));
		
		timeline.setCycleCount(Animation.INDEFINITE);
		timeline.play();
	}

	public void draw() {
		double width = getWidth();
		double height = getHeight();
		GraphicsContext gc = getGraphicsContext2D();
		gc.clearRect(0, 0, width, height);
		gc.setStroke(Color.BLACK);
		Font font = Font.font("Monospaced", FONT_SIZE);
		gc.setFont(font);
		
		if(drops == null || drops.length == 0) {
			drops = new RainDrop[(int) (getWidth() / FONT_SIZE)];
			for (int i = 0; i < drops.length; i++) {
				drops[i] = new RainDrop(i * FONT_SIZE, (int) getHeight(), getAHash());
			}
		}
		
		for (int i = 0; i < drops.length; i++) {
			if (drops[i].isOffScreen()) {
				drops[i] = new RainDrop(i * FONT_SIZE, (int) getHeight(), getAHash());
			}
			drops[i].draw(gc);
		}
	}

	@Override
	public boolean isResizable() {
		return true;
	}

	@Override
	public double prefWidth(double height) {
		return getWidth();
	}

	@Override
	public double prefHeight(double width) {
		return getHeight();
	}
	
	public void setMain(Main main) {
		this.main = main;
		
		main.getTokhnStub().streamBlocks(new StreamObserver<BlockModel>() {
			@Override
			public void onCompleted() {
				System.out.println("Block stream completed");
			}

			@Override
			public void onError(Throwable t) {
				System.err.println(t);
			}

			@Override
			public void onNext(BlockModel blockModel) {
				System.out.println(blockModel);
				blockIds.add(blockModel.getHash());
			}
		});
	}
	
	private String getAHash() {
		String head = blockIds.poll();
		if(head == null) {
			return "";
		} else {
			return head;
		}
		/*
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(Instant.now().toString().getBytes());
		    byte[] digest = md.digest();
		    return DatatypeConverter.printHexBinary(digest);
		} catch (NoSuchAlgorithmException e) {
			//nop
		}
		return "";
		*/
	}
}