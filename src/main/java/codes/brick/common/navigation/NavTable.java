package codes.brick.common.navigation;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;

import java.util.ArrayList;
import java.util.List;

public class NavTable extends Table {
  private final List<List<Actor>> navList;
  private int currentRow = 0;

  /**
   * Like a normal table, but record information for Controller navigation.
   * @param skin Skin to use for table
   */
  public NavTable(Skin skin) {
    super(skin);
    navList = new ArrayList<>();
    navList.add(new ArrayList<>());
  }

  /**
   * Get the navlist, used for controller navigation support.
   * @return Navlist for controller navigation
   */
  public List<List<Actor>> getNavList() {
    if (!navList.get(currentRow).isEmpty()) {
      return navList;
    }
    List<List<Actor>> newList = new ArrayList<>(navList);
    newList.remove(currentRow);
    return newList;
  }

  @Override
  public <T extends Actor> Cell<T> add(T actor) {
    navList.get(currentRow).addAll(getButtons(actor));
    return super.add(actor);
  }

  @Override
  public Cell row()  {
    if (!navList.get(currentRow).isEmpty()) {
      currentRow++;
      navList.add(new ArrayList<>());
    }
    return super.row();
  }

  private static List<Actor> getButtons(Actor actor) {
    List<Actor> actors = new ArrayList<>();
    // CheckBoxes are instances of TextButton for some reason
    if (actor instanceof TextButton) {
      actors.add(actor);
    } else if (actor instanceof HorizontalGroup || actor instanceof VerticalGroup) {
      for (Actor subActor : ((WidgetGroup)actor).getChildren()) {
        actors.addAll(getButtons(subActor));
      }
    }
    return actors;
  }
}
