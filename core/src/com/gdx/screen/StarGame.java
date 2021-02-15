/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package com.gdx.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.gdx.common.Constant;
import com.gdx.common.LogW;
import com.gdx.common.LogW.ALog;
import com.gdx.common.Tool;

public class StarGame extends Game {
	
	boolean firstTimeCreate = true;
	static int type=2;
	
	public StarGame(ALog mALog_) {
		LogW.setALog(mALog_);
	}

	@Override
	public void create () {
		/*Stage stage = null;
		stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));*/
		
		float GameWidth=Constant.GameWidth;
		float GameHeight=Constant.GameHeight;
		Constant.perW= Gdx.graphics.getWidth()/GameWidth;
		Constant.perH= Gdx.graphics.getHeight()/GameHeight;
		
		Constant.TextureBlack=Tool.loadTexture_raw("colors/BLACK.png");
		Constant.TextureBlue=Tool.loadTexture_raw("colors/BLUE.png");
		Constant.TextureGreen=Tool.loadTexture_raw("colors/GREEN.png");
		Constant.TextureRed=Tool.loadTexture_raw("colors/RED.png");
		Constant.TextureWhite=Tool.loadTexture_raw("colors/WHITE.png");
		Constant.TextureYellow=Tool.loadTexture_raw("colors/YELLOW.png");
		
		setScreen(new GameScreen(this));
	}
	
	@Override
	public void render() {
		super.render();
		//----System.out.println("--BuyuGame--render--FPS:-->>"+Gdx.graphics.getFramesPerSecond());
	}

	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();

		getScreen().dispose();
	}
}
