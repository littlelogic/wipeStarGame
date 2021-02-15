package com.gdx.others;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.Random;

public class StarParticleManage {

	private  ArrayList<StarEmitter> StarEmitterList=new ArrayList<StarEmitter>();
	
	public StarParticleManage(TextureRegion mRegion, Color mColor, float xCentre, float yCentre, int star_num){
		StarEmitterList.clear();
		Random random = new Random(System.currentTimeMillis());//---待
		int v_x_temp;
		float v_x;
		float v_y;
		float time;
		float rotation_start;
		float rotation_differ;
		
		float scale_start;
		float scale_differ;
		
		float alpha_start;
		float alpha_differ;
		
		for(int i=0;i<star_num;i++){
			rotation_start=random.nextInt(360);
			rotation_differ=random.nextInt(720)+100;
			//--rotation_differ=random.nextInt(720)+1000;
			//--v_x_temp=random.nextInt(3000)+3000;
			v_x_temp=random.nextInt(1000)+10;
			if( v_x_temp%2==0){
				v_x_temp=-v_x_temp;
			}else{
				rotation_differ=-rotation_differ;
			}
			v_x=v_x_temp;
			
			v_y=random.nextInt(1000)+500;
			time=random.nextInt(1000)+1000;
			time=time/1000f;

			scale_start=1f;
			scale_differ=-random.nextInt(3)/10f;
			
			alpha_start=random.nextInt(5)/10f+0.5f;
			alpha_differ=-0.5f;
			
			StarEmitter mStarEmitter=new StarEmitter(mRegion,mColor,i,30,30,xCentre,yCentre,v_x,v_y,-3000,time,
					rotation_start,rotation_differ,
					scale_start,scale_differ,
					alpha_start,alpha_differ);
			StarEmitterList.add(mStarEmitter);
		}
	}

	public int draw (SpriteBatch batcher, float deltaTime) {
		for(int i=0;i<StarEmitterList.size();){
			if(StarEmitterList.get(i).draw(batcher, deltaTime)){
				StarEmitterList.remove(i);
			}else{
				i++;
			}
		}
		return StarEmitterList.size();
	}
	
}
