package com.gdx.others;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class StarEmitter {
	
	int  index_mark;
	
	TextureRegion mRegion;
	Color mColor;
	
	float width_Region;
	float height_Region;
	
	float width_Region_half;
	float height_Region_half;
	
	float width_self=24;
	float height_self=24;
	
	/*public float xCentre;
	public float yCentre;*/
	
	public float xLeft;
	public float yBottom;

	public float v_x;
	public float v_y;

	public float gravity_y=-3000;
	
	public float time_start;
	public float time_per;
	public final float time_total;
	public boolean time_over=false;
	
	public  float rotation_start;
	public  float rotation_time;
	public  float rotation_differ;
	
	public  float scale_original;
	public  float scale_start;
	public  float scale_differ;
	public  float scale_time;
	
	public  float alpha_start;
	public  float alpha_differ;
	public  float alpha_time;
	
	public StarEmitter(TextureRegion mRegion_, Color mColor_, int index_mark_, float width_self_, float height_self_, float xCentre, float yCentre,
                       float v_x_, float v_y_, float gravity_y_, float time_total_,
                       float rotation_start_, float rotation_differ_,
                       float scale_start_, float scale_differ_,
                       float alpha_start_, float alpha_differ_){
		
		
		/*LogW.i("zddz_001", "--StarEmitter--v_x_-->"+v_x_);
		LogW.i("zddz_001", "--StarEmitter--v_y_-->"+v_y_);
		LogW.i("zddz_001", "--StarEmitter--gravity_y_-->"+gravity_y_);
		LogW.i("zddz_001", "--StarEmitter--time_total_-->"+time_total_);*/
		
		mRegion=mRegion_;
		mColor=mColor_;
		index_mark=index_mark_;
		width_Region=mRegion.getRegionWidth();
		height_Region=mRegion.getRegionHeight();
		width_Region_half=width_Region/2f;
		height_Region_half=height_Region/2f;
		width_self=width_self_;
		height_self=height_self_;
		
		xLeft=xCentre-width_Region_half;
		yBottom=yCentre-height_Region_half;

		v_x=v_x_;
		v_y=v_y_;
		gravity_y=gravity_y_;
		time_total=time_total_;
		
		rotation_start=rotation_start_;
		rotation_differ=rotation_differ_;
		
		scale_original=width_self_/width_Region;
		scale_start=scale_start_*scale_original;
		scale_differ=scale_differ_*scale_original;
		
		alpha_start=alpha_start_;
		alpha_differ=alpha_differ_;
		
		time_start=0;
		time_over=false;
	}
	

	public boolean draw (SpriteBatch batcher, float deltaTime) {
		time_start+=deltaTime;
		if(time_start>=time_total){
			//周期结束
			time_over=true;
			return true;
		}

		time_per=time_start/time_total;
		xLeft = xLeft + v_x * deltaTime;

		float v_y_now = v_y + gravity_y * deltaTime;
		float v_y_a = (v_y + v_y_now)/2;
		yBottom = yBottom + v_y_a * deltaTime;
		v_y = v_y_now;

		rotation_time = rotation_start + time_per * rotation_differ;
		alpha_time    = alpha_start    + time_per * alpha_differ;
		scale_time    = scale_start    + time_per * scale_differ;
		
		batcher.setColor(mColor.set(mColor.r, mColor.g, mColor.b, alpha_time));
		batcher.draw(mRegion,xLeft, yBottom,
				width_Region_half, height_Region_half, 
				width_Region, height_Region, 
				scale_time, scale_time, rotation_time);
		time_over=false;
		return false;
	}

	//vt=v0+at是指物体在做匀加速运动时速度的t时刻速度的计算方法,
	public boolean draw_old (SpriteBatch batcher, float deltaTime) {
		time_start+=deltaTime;
		if(time_start>=time_total){
			//周期结束
			//---LogW.e("zddz_001", "--StarEmitter--draw--周期结束--rotation_time-->"+rotation_time);
			time_over=true;
			return true;
		}

		time_per=time_start/time_total;
		xLeft = xLeft + v_x * deltaTime;

		//-----
		v_y = v_y + gravity_y * deltaTime;
		yBottom = yBottom + v_y * deltaTime;
		//-----

		rotation_time = rotation_start + time_per * rotation_differ;
		alpha_time    = alpha_start    + time_per * alpha_differ;
		scale_time    = scale_start    + time_per * scale_differ;

		if(index_mark==2){
			/*LogW.e("zddz_001", "--StarEmitter--draw--002--rotation_start-->"+rotation_start);
			LogW.e("zddz_001", "--StarEmitter--draw--002--rotation_differ-->"+rotation_differ);
			LogW.e("zddz_001", "--StarEmitter--draw--002--time_per-->"+time_per);
			LogW.e("zddz_001", "--StarEmitter--draw--002--rotation_time-->"+rotation_time);*/

			/*LogW.e("zddz_001", "--StarEmitter--draw--002--scale_start-->"+scale_start);
			LogW.e("zddz_001", "--StarEmitter--draw--002--scale_differ-->"+scale_differ);
			LogW.e("zddz_001", "--StarEmitter--draw--002--scale_time-->"+scale_time);*/
		}

		batcher.setColor(mColor.set(mColor.r, mColor.g, mColor.b, alpha_time));
		//--batcher.setColor(mColor.set(mColor.r, mColor.g, mColor.b, 1f));
		batcher.draw(mRegion,xLeft, yBottom,
				width_Region_half, height_Region_half,
				width_Region, height_Region,
				//--scale_original, scale_original, rotation_time);
				scale_time, scale_time, rotation_time);

		time_over=false;
		return false;
	}

}
