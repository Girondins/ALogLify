package com.examen.aloglife;

/**
 * Created by Girondins on 26/02/17.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.Skeleton;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;

public class Loggi extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;

    private TextureAtlas atlas;
    private Skeleton skeleton;
    private AnimationState state;

    private float centerX,centerY;

    public Loggi(float centerX, float centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void create () {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setBoundingBoxes(false);
        debugRenderer.setRegionAttachments(false);

        atlas = new TextureAtlas(Gdx.files.internal("loggi/LoggiBoy_tex.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(1.5f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("loggi/LoggiBoy.json"));

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(centerX, centerY);

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(1f); // Slow all animations down to 50% speed.

        // Queue animations on track 0.
        state.setAnimation(0, "animtion0", true);
     //   state.addAnimation(0, "run", true, 0);
    }

    public void render () {
        state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
        skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.

        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);
        batch.begin();
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        batch.end();

    //    debugRenderer.draw(skeleton); // Draw debug lines.
    }

    public void resize (int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
    }

    public void dispose () {
        atlas.dispose();
    }

}