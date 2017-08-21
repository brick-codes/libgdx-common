package codes.brick.common.navigation;

import codes.brick.common.controllers.Xbox;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.Pools;
import java.util.List;

public class MenuControllerInputWatcher implements ControllerListener, InputProcessor {

  private List<List<Actor>> nav;
  private int currentRow = 0;
  private int currentCol = 0;
  private int lastCol = 0;

  private static final float MAX_COOLDOWN = 0.2f;
  private float cooldown = 0;

  private static final float DEAD_ZONE = 0.4f;
  private static final float N_DEAD_ZONE = DEAD_ZONE * -1;

  public MenuControllerInputWatcher(List<List<Actor>> navList) {
    nav = navList;
  }

  // TODO: configurable controls

  @Override
  public boolean buttonDown(Controller controller, int buttonCode) {
    if (nav.size() == 0) {
      return false;
    }
    if (buttonCode == Xbox.A) {
      clickButton(getCurrentActor());
      return releaseButton(getCurrentActor());
    }
    return false;
  }

  @Override
  public boolean keyDown(int keyCode) {
    if (nav.size() == 0) {
      return false;
    }
    if (keyCode == Input.Keys.ENTER) {
      clickButton(getCurrentActor());
      return releaseButton(getCurrentActor());
    } else if (keyCode == Input.Keys.UP || keyCode == Input.Keys.W) {
      deselectButton(getCurrentActor());
      return goUp();
    } else if (keyCode == Input.Keys.DOWN || keyCode == Input.Keys.S) {
      deselectButton(getCurrentActor());
      return goDown();
    } else if (keyCode == Input.Keys.LEFT || keyCode == Input.Keys.A) {
      deselectButton(getCurrentActor());
      return goLeft();
    } else if (keyCode == Input.Keys.RIGHT || keyCode == Input.Keys.D) {
      deselectButton(getCurrentActor());
      return goRight();
    }
    return false;
  }

  private boolean clickButton(Actor button) {
    InputEvent event = Pools.obtain(InputEvent.class);
    event.setType(InputEvent.Type.touchDown);
    event.setButton(Input.Buttons.LEFT);

    button.fire(event);
    boolean handled = event.isHandled();
    Pools.free(event);
    return handled;
  }

  private boolean releaseButton(Actor button) {
    InputEvent event = Pools.obtain(InputEvent.class);
    event.setType(InputEvent.Type.touchUp);
    event.setButton(Input.Buttons.LEFT);

    button.fire(event);
    boolean handled = event.isHandled();
    Pools.free(event);
    return handled;
  }

  @Override
  public boolean axisMoved(Controller controller, int axisCode, float value) {
    if (cooldown > 0 || nav.size() == 0 || (value < DEAD_ZONE && value > N_DEAD_ZONE)) {
      return false;
    }

    cooldown = MAX_COOLDOWN;
    deselectButton(getCurrentActor());

    if (axisCode == Xbox.L_STICK_HORIZONTAL_AXIS) {
      if (value > 0) {
        return goRight();
      } else if (value < 0) {
        return goLeft();
      }
    } else if (axisCode == Xbox.L_STICK_VERTICAL_AXIS) {
      if (value > 0) {
        return goDown();
      } else if (value < 0) {
        return goUp();
      }
    }

    return false;
  }

  private boolean goUp() {
    if (--currentRow < 0) {
      currentRow = 0;
    } else {
      currentCol = lastCol;
      if (currentCol >= nav.get(currentRow).size()) {
        currentCol = nav.get(currentRow).size() - 1;
      }
    }
    return selectButton(getCurrentActor());
  }

  private boolean goDown() {
    if (++currentRow == nav.size()) {
      currentRow = nav.size() - 1;
    } else {
      currentCol = lastCol;
      if (currentCol >= nav.get(currentRow).size()) {
        currentCol = nav.get(currentRow).size() - 1;
      }
    }
    return selectButton(getCurrentActor());
  }

  private boolean goLeft() {
    if (--currentCol < 0) {
      currentCol = 0;
    }
    lastCol = currentCol;
    return selectButton(getCurrentActor());
  }

  private boolean goRight() {
    if (++currentCol == nav.get(currentRow).size()) {
      currentCol = nav.get(currentRow).size() - 1;
    }
    lastCol = currentCol;
    return selectButton(getCurrentActor());
  }

  private boolean selectButton(Actor button) {
    InputEvent event = Pools.obtain(InputEvent.class);
    event.setType(InputEvent.Type.enter);

    button.fire(event);
    button.setColor(Color.RED);
    boolean handled = event.isHandled();
    Pools.free(event);
    return handled;
  }

  private boolean deselectButton(Actor button) {
    InputEvent event = Pools.obtain(InputEvent.class);
    event.setType(InputEvent.Type.exit);

    button.fire(event);
    button.setColor(Color.WHITE);
    boolean handled = event.isHandled();
    Pools.free(event);
    return handled;
  }

  private Actor getCurrentActor() {
    return nav.get(currentRow).get(currentCol);
  }

  /**
   * Tick down cooldown, which prevents users from moving through menus accidentally.
   *
   * @param delta delta time to subtract from cooldown.
   */
  public void cooldown(float delta) {
    cooldown -= delta;
    if (cooldown < 0) {
      cooldown = 0;
    }
  }

  public void updateNavList(List<List<Actor>> navList) {
    this.nav = navList;
  }

  // Dumb implementations we don't need below.
  // -----------------------------------------

  @Override
  public boolean buttonUp(Controller controller, int buttonCode) {
    return false;
  }

  @Override
  public boolean keyUp(int keyCode) {
    return false;
  }

  @Override
  public boolean keyTyped(char key) {
    return false;
  }

  @Override
  public boolean mouseMoved(int x, int y) {
    return false;
  }

  @Override
  public boolean scrolled(int amt) {
    return false;
  }

  @Override
  public boolean touchUp(int x, int y, int z, int q) {
    return false;
  }

  @Override
  public boolean touchDown(int x, int y, int z, int q) {
    return false;
  }

  @Override
  public boolean touchDragged(int x, int y, int z) {
    return false;
  }

  @Override
  public boolean povMoved(Controller controller, int povCode, PovDirection direction) {
    return false;
  }

  @Override
  public boolean xSliderMoved(Controller controller, int povCode, boolean whatever) {
    return false;
  }

  @Override
  public boolean ySliderMoved(Controller controller, int povCode, boolean whatever) {
    return false;
  }

  @Override
  public boolean accelerometerMoved(Controller controller, int povCode, Vector3 vector3) {
    return false;
  }

  @Override
  public void connected(Controller controller) {}

  @Override
  public void disconnected(Controller controller) {}
}
