package org.suai.poker.graphics;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

public class ImageScroller implements Runnable {
	private Stage stage;
	private ImageView imageView;
	private List<Image> imageList;

	public ImageScroller(Stage stage, ImageView imageView, List<Image> imageList) {
		this.stage = stage;
		this.imageView = imageView;
		this.imageList = imageList;
	}

	@Override
	public void run() {
		while (stage.isShowing()) {
			try {
				Integer random = (int) (Math.random() * 52);
				scroll(imageView, imageList.get(random));
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void scroll(ImageView imageView, Image image) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				imageView.setImage(image);
			}
		});
	}

}
