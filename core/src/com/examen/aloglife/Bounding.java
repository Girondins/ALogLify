package com.examen.aloglife;

/**
 * Created by Girondins on 26/02/17.
 */

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;

import spine.AnimationState;
import spine.AnimationStateData;
import spine.BoundingBoxAttachment;
import spine.Skeleton;
import spine.SkeletonBounds;
import spine.SkeletonData;
import spine.SkeletonJson;
import spine.SkeletonMeshRenderer;
import spine.SkeletonRendererDebug;

public class Bounding extends ApplicationAdapter {
    OrthographicCamera camera;
    PolygonSpriteBatch batch;
    SkeletonMeshRenderer renderer;
    SkeletonRendererDebug debugRenderer;
    SkeletonBounds bounds;

    TextureAtlas atlas, loggiAtl;
    Skeleton skeleton, loggiSkel;
    AnimationState state,loggiState;

    private float centerX,centerY;

    public Bounding(float centerX, float centerY){
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

        atlas = new TextureAtlas(Gdx.files.internal("boundingbox/boggi_tex.atlas"));
        SkeletonJson json = new SkeletonJson(atlas); // This loads skeleton JSON data, which is stateless.
        json.setScale(1f); // Load the skeleton at 50% the size it was in Spine.
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("boundingbox/boggi.json"));


        skeleton = new Skeleton(skeletonData); // Skeleton holds skeleton state (bone positions, slot attachments, etc).
        skeleton.setAttachment("body-bb","body_boundingBox");
        skeleton.setPosition(centerX,170);
        bounds = new SkeletonBounds();

        AnimationStateData stateData = new AnimationStateData(skeletonData); // Defines mixing (crossfading) between animations.

        state = new AnimationState(stateData); // Holds the animation state for a skeleton (current animation, time, etc).
        state.setTimeScale(0.6f); // Slow all animations down to 60% speed.

        // Queue animations on tracks 0 and 1.
        state.setAnimation(0, "IdleNormal", true);
        state.addAnimation(0, "waveNormal", false, 2);


        loggiAtl = new TextureAtlas(Gdx.files.internal("loggi/LoggiBoy_tex.atlas"));
        SkeletonJson jsonAtl = new SkeletonJson(loggiAtl); // This loads skeleton JSON data, which is stateless.
        jsonAtl.setScale(0.3f); // Load the skeleton at 60% the size it was in Spine.
        SkeletonData skeletonDataLog = jsonAtl.readSkeletonData(Gdx.files.internal("loggi/LoggiBoy.json"));

        loggiSkel  = new Skeleton(skeletonDataLog);
        loggiSkel.setPosition(150,350);

     // Keys in higher tracks override the pose from lower tracks.
        Gdx.input.setInputProcessor(new InputAdapter() {
            final Vector3 point = new Vector3();

            public boolean touchDown (int screenX, int screenY, int pointer, int button) {
                camera.unproject(point.set(screenX, screenY, 0)); // Convert window to world coordinates.
                bounds.update(skeleton, true); // Update SkeletonBounds with current skeleton bounding box positions.

                System.out.println("x : " + screenX + " y : " + screenY);
                if (bounds.aabbContainsPoint(point.x, point.y)) { // Check if inside AABB first. This check is fast.
                    BoundingBoxAttachment hit = bounds.containsPoint(point.x, point.y); // Check if inside a bounding box.
                    if (hit != null) {
                        System.out.println(" WOOOOOO O O OO ");
                        state.setAnimation(0, "jumpNormal", false); // Set animation on track 0 to jump.
                        state.addAnimation(0, "IdleNormal", true, 0); // Queue run to play after jump.
                    }
                }
                return true;
            }

            public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            //    skeleton.findSlot("head").getColor().set(Color.WHITE);
                return true;
            }

            public boolean keyDown (int keycode) {
           //     state.setAnimation(0, "jump", false); // Set animation on track 0 to jump.
            //    state.addAnimation(0, "run", true, 0); // Queue run to play after jump.
                return true;
            }
        });



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
        renderer.draw(batch, loggiSkel);
        renderer.draw(batch, skeleton); // Draw the skeleton images.
        batch.end();

        debugRenderer.draw(skeleton); // Draw debug lines.
    }

    public void resize (int width, int height) {
        camera.setToOrtho(false); // Update camera with new size.
    }

    public void dispose () {
        atlas.dispose();
    }

}