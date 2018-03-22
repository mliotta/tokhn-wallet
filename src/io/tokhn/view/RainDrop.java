package io.tokhn.view;

import java.util.Arrays;
import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class RainDrop {
	private final static int FONT_SIZE = 16;
	private final int maxHeight;
	private Random rng = new Random();
	private int velocity;
	private int x, y;
	private String display;
	
	RainDrop(int x, int maxHeight, String display) {
		this.x = x;
		this.maxHeight = maxHeight;
		this.display = display;
		velocity = getRandomInteger(1, 5);
		this.y = (-1) * display.length() * FONT_SIZE;
	}
	
	public void draw(GraphicsContext gc) {
		int fontSize = (int) gc.getFont().getSize();
		String toPrint = null;
		//draw each character starting from the last to the first from top to bottom
		//randomly change between utf8 and hex
		for(int itr = display.length() - 1; itr >= 0; itr--) {
			if(rng.nextBoolean()) {
				toPrint = display.substring(itr, itr + 1).toUpperCase();
			} else {
				toPrint = display.substring(itr, itr + 1).toLowerCase();
			}
			if(itr == 0) {
				//first character
				gc.setStroke(Color.rgb(253, 104, 25));
			} else {
				// regular character
				gc.setStroke(Color.rgb(66, 198, 255));
			}
			gc.strokeText(toPrint, x, y + ((display.length() - itr) * fontSize));
		}
		y += velocity;
	}

	public char getRandomCharacter() {
		return (char) (rng.nextInt(26) + 'a');
	}

	public int getRandomInteger(int min, int max) {
		int randomNum = rng.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public char[] getFixedCharArrayFromString(String text, int length) {
		if (text.length() == length) {
			return text.toCharArray();
		} else if (text.length() < length) {
			char tmp[] = new char[length];
			Arrays.fill(tmp, ' ');
			text.getChars(0, text.length(), tmp, 0);
			return tmp;
		} else {
			return text.substring(0, length).toCharArray();
		}
	}

	public boolean isOffScreen() {
		return (y > maxHeight);
	}
}