package com.gdx.others;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

public class WStage extends Stage {

	
	public WStage(Viewport viewport, Batch batch) {
		
		if (viewport == null) throw new IllegalArgumentException("viewport cannot be null.");
		if (batch == null) throw new IllegalArgumentException("batch cannot be null.");
		this.setViewport(viewport);
//		this.batch = batch;
//
//		root = new Group();
//		root.setStage(this);
	}
}
