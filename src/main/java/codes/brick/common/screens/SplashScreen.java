package codes.brick.common.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class SplashScreen implements Screen {
  public static final float DURATION = 1.5f;

  private final Viewport viewport;

  private final SpriteBatch batch;

  private final Texture logo;

  public float accumulated = 0;

  public SplashScreen(Texture logo, SpriteBatch batch) {
    this.logo = logo;
    this.batch = batch;
    viewport = new FitViewport(logo.getWidth() * 1.1f, logo.getHeight() * 1.1f);
  }

  @Override
  public void show() {
    Gdx.gl.glClearColor(255f, 255f, 255f, 1);
  }

  @Override
  public void render(float delta) {
    batch.begin();
    batch.draw(logo, -logo.getWidth() / 2, -logo.getHeight() / 2);
    batch.end();
    accumulated += delta;
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    batch.setProjectionMatrix(viewport.getCamera().combined);
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {
    Gdx.gl.glClearColor(0f, 0f, 0f, 1);
  }

  @Override
  public void dispose() {
    // batch is disposed elsewhere
  }
}
