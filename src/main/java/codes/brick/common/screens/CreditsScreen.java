package codes.brick.common.screens;

import codes.brick.common.navigation.MenuControllerInputWatcher;
import codes.brick.common.navigation.NavTable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class CreditsScreen implements Screen {
  private final Stage stage;

  public boolean finished;

  private MenuControllerInputWatcher cp;

  /**
   * Create a new credits screen.
   * @param skin Skin to use for styling the screen
   */
  public CreditsScreen(Skin skin, Array<String> credits) {
    stage = new Stage(new FitViewport(1000, 360));
    NavTable table = new NavTable(skin);
    table.setFillParent(true);
    stage.addActor(table);
    {
      final List creditList = new List(skin);
      creditList.setItems(credits);
      final ScrollPane scrollPane = new ScrollPane(creditList);
      table.add(scrollPane);
    }
    table.row();
    {
      final Button returnButton = new TextButton("Done", skin);
      table.add(returnButton).pad(5);
      returnButton.pad(10);
      returnButton.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float posX, float posY) {
          finished = true;
        }
      });
    }
    cp = new MenuControllerInputWatcher(table.getNavList());
  }

  @Override
  public void show() {
    stage.getViewport().apply();
    Gdx.input.setInputProcessor(new InputMultiplexer(cp, stage));
    Controllers.addListener(cp);
  }

  @Override
  public void render(float delta) {
    cp.cooldown(delta);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
  }

  @Override
  public void pause() {
  }

  @Override
  public void resume() {
  }

  @Override
  public void hide() {
    Controllers.removeListener(cp);
  }

  @Override
  public void dispose() {
    stage.dispose();
  }
}
