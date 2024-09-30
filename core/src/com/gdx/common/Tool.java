package com.gdx.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

public class Tool {
	
	public static Texture loadTexture (String file) {
//		texture = new Texture(Gdx.files.internal("data/badlogic.jpg"), false);
//		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		
//		items = loadTexture("data/items.png");
//		mainMenu = new TextureRegion(items, 0, 224, 300, 110);
		
		//return new Texture(Gdx.files.internal(file));
		Texture mTexture=new Texture(Gdx.files.internal(file));
		mTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		return mTexture;
	}
	
	public static Texture loadTexture_raw (String file) {
		Texture mTexture=new Texture(Gdx.files.internal(file));
		//---Texture mTexture=new Texture(Gdx.files.absolute(file));
		return mTexture;
	}
	
	//========================================================
	

	
	
	
	
}
