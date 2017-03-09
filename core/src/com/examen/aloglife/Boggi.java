package com.examen.aloglife;

/**
 * Created by Girondins on 26/02/17.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.Skeleton;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonMeshRenderer;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;

public class Boggi extends ApplicationAdapter {
    OrthographicCamera camera;
    PolygonSpriteBatch batch;
    SkeletonMeshRenderer renderer;
    SkeletonRendererDebug debugRenderer;

    TextureAtlas atlas, loggiAtl;
    Skeleton skeleton, loggiSkel;
    AnimationState state,loggiState;

    private float centerX,centerY;

    public Boggi(float centerX, float centerY){
        this.centerX = centerX;
        this.centerY = centerY;
    }

    public void create () {
        camera = new OrthographicCamera();
        batch = new PolygonSpriteBatch(); // Required to render meshes. SpriteBatch can't render meshes.
        renderer = new SkeletonMeshRenderer();
        renderer.setPremultipliedAlpha(true);
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setMeshTriangles(false);
        debugRenderer.setRegionAttachments(false);
        debugRenderer.setMeshHull(false);

        atlas = new TextureAtlas(Gdx.files.internal("boggi/boggi_tex.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(1f); // Load the skeleton at 50% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("boggi/boggi.json"));

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(centerX, 170);

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(0.6f); // Slow all animations down to 60% speed.

        // Queue animations on tracks 0 and 1.
        state.setAnimation(0, "idleAnimation", true);



        loggiAtl = new TextureAtlas(Gdx.files.internal("loggi/LoggiBoy_tex.atlas"));
        SkeletonJson jsonAtl = new SkeletonJson(loggiAtl); // This loads skeleton JSON data, which is stateless.
        jsonAtl.setScale(0.3f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonDataLog = jsonAtl.readSkeletonData(Gdx.files.internal("loggi/LoggiBoy.json"));

        loggiSkel  = new Skeleton(skeletonDataLog);
        loggiSkel.setPosition(150,350);

     // Keys in higher tracks override the pose from lower tracks.
    }

    public void render () {
        state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state.apply(skeleton); // Poses skeleton using current animations. This sets the bones' local SRT.
        skeleton.updateWorldTransform(); // Uses the bones' local SRT to compute their world SRT.
        loggiSkel.updateWorldTransform();
        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        renderer.draw(batch, loggiSkel);
        batch.end();
    }

    public void resize (int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
    }

    public void dispose () {
        atlas.dispose();
    }

}