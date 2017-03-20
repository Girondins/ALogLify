package com.examen.aloglife;

/**
 * Created by Girondins on 26/02/17.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.BoundingBoxAttachment;
import spine.Skeleton;
import spine.SkeletonBounds;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonRenderer;
import spine.SkeletonRendererDebug;

public class SimpleTest1 extends ApplicationAdapter {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private SkeletonRenderer renderer;
    private SkeletonRendererDebug debugRenderer;

    SkeletonBounds bounds ;

    private TextureAtlas atlas;
    private Skeleton skeleton;    private Skeleton skeleton2;
    private AnimationState state;

    public void create () {
        camera = new OrthographicCamera();
        batch = new SpriteBatch();
        renderer = new SkeletonRenderer();
        renderer.setPremultipliedAlpha(true); // PMA results in correct blending without outlines.
        debugRenderer = new SkeletonRendererDebug();
        debugRenderer.setBoundingBoxes(false);
        debugRenderer.setRegionAttachments(false);

        atlas = new TextureAtlas(Gdx.files.internal("spineboy/spineboy.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(0.6f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("spineboy/spineboy.json"));

        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setPosition(250, 20);
        skeleton.setAttachment("head-bb", "head");
        skeleton2 = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton2.setPosition(500, 20);


        bounds = new SkeletonBounds();

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.
        stateData.setMix("run", "shoot", 0.2f);
        stateData.setMix("shoot", "run", 0.2f);

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(0.5f); // Slow all animations down to 50% speed.

        // Queue animations on track 0.
        state.setAnimation(0, "run", true);
        state.addAnimation(0, "jump", false, 2); // Jump after 2 seconds.
        state.addAnimation(0, "run", true, 0); // Run after the jump.

        Gdx.input.setInputProcessor(new InputAdapter() {
            final Vector3 point = new Vector3();

            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                camera.unproject(point.set(screenX, screenY, 0)); // Convert window to world coordinates.
                bounds.update(skeleton, true); // Update SkeletonBounds with current skeleton bounding box positions.
                if (bounds.aabbContainsPoint(screenX, screenY)) { // Check if inside AABB first. This check is fast.
                    BoundingBoxAttachment hit = bounds.containsPoint(screenX, screenY);
                    if (hit != null) {
                        state.setAnimation(0, "shoot", false); // Set animation on track 0 to jump.
                        state.addAnimation(0, "run", true, 0); // Queue run to play after jump.
                    }
                }
                return true;
            }

            public boolean touchUp (int screenX, int screenY, int pointer, int button) {
                //        state.setAnimation(0, "jumpNormal", false); // Set animation on track 0 to jump.
                //         state.addAnimation(0, "idlenormal", true, 0); // Queue run to play after jump.
                return true;
            }

            public boolean keyDown (int keycode) {
                state.setAnimation(0, "jumpNormal", false); // Set animation on track 0 to jump.
                state.addAnimation(0, "idlenormal", true, 0); // Queue run to play after jump.
                return true;
            }
        });




    }

    public void render () {
        state.update(Gdx.graphics.getDeltaTime()); // Update the animation time.

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        state.apply(skeleton);
        state.apply(skeleton2);// Poses skeleton using current animations. This sets the bones' local SRT.
        skeleton.updateWorldTransform();
        skeleton2.updateWorldTransform();// Uses the bones' local SRT to compute their world SRT.

        // Configure the camera, SpriteBatch, and SkeletonRendererDebug.
        camera.update();
        batch.getProjectionMatrix().set(camera.combined);
        debugRenderer.getShapeRenderer().setProjectionMatrix(camera.combined);

        batch.begin();
        renderer.draw(batch, skeleton);
        renderer.draw(batch, skeleton2);// Draw the skeleton images.
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