/*
 * Copyright 2018 Matt Liotta
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.tokhn.view;

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

	public int getRandomInteger(int min, int max) {
		int randomNum = rng.nextInt((max - min) + 1) + min;
		return randomNum;
	}

	public boolean isOffScreen() {
		return (y > maxHeight);
	}
}