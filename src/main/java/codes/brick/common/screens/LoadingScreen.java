package codes.brick.common.screens;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.StringBuilder;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class LoadingScreen implements Screen {

  private final Viewport viewport;

  private final SpriteBatch batch;

  private final BitmapFont font;

  private StringBuilder text;
  private final GlyphLayout layout;

  private float accumulated = 0;

  // We don't want to use a global random here,
  // because if we want to re-run the game with the same seed to get
  // the same results, the amount of time it takes to load shouldn't throw it off.
  private final Random rand = new Random();

  private final float startingWidth;

  /**
   * Display some fun text while we load assets.
   * @param font Font to display loading text.
   * @param batch Batch to draw with.
   */
  public LoadingScreen(BitmapFont font, SpriteBatch batch) {
    this.font = font;
    this.batch = batch;
    text = new StringBuilder(10);
    text.append("Loading");
    layout = new GlyphLayout(font, text);
    startingWidth = layout.width;
    viewport = new FitViewport(layout.width * 2, layout.height * 5);
  }

  @Override
  public void show() {
  }

  @Override
  public void render(float delta) {
    batch.begin();
    font.draw(batch, layout, (viewport.getWorldWidth() - startingWidth) / 2,
        (viewport.getWorldHeight() + layout.height) / 2);
    batch.end();
    accumulated += delta;
    while (accumulated >= 0.2) {
      if (text.length == 10) {
        font.setColor(randomColor());
        text.setLength(0);
        text.append("Loading");
      } else {
        text.append(".");
      }
      accumulated -= 0.2;
    }
    layout.setText(font, text);
  }

  private Color randomColor() {
    return new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat(), 1);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
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
  }

  @Override
  public void dispose() {
    // We dispose of batch elsewhere
  }
}
