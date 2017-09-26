package codes.brick.common;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.utils.BufferUtils;
import com.badlogic.gdx.utils.ScreenUtils;
import java.time.LocalDate;

public class ScreenshotManager {
  private static final String SCREENSHOT_DIR = "My Games/%s/Screenshots/";
  private static final String FILE_EXTENSION = ".png";

  /** Take a screenshot of the current game, saving it to predefined file path.
   * @param nameOfGame Name of the game, used in the path
   * */
  public static void takeScreenshot(String nameOfGame) {
    byte[] pixels =
        ScreenUtils.getFrameBufferPixels(
            0, 0, Gdx.graphics.getBackBufferWidth(), Gdx.graphics.getBackBufferHeight(), true);
    Pixmap pixmap =
        new Pixmap(
            Gdx.graphics.getBackBufferWidth(),
            Gdx.graphics.getBackBufferHeight(),
            Pixmap.Format.RGBA8888);
    BufferUtils.copy(pixels, 0, pixmap.getPixels(), pixels.length);
    String fileName = String.format(SCREENSHOT_DIR, nameOfGame) + LocalDate.now();
    String filePath = fileName + FILE_EXTENSION;
    // This is O(N) where N is number of screenshots taken today
    // we could improve this by caching the last index we found
    // alternate algorithm: get list of file names, see the highest number, save highest number+1
    // not sure that helps anyway...
    for (int i = 0; Gdx.files.external(filePath).exists(); i++) {
      filePath = fileName + "-" + String.format("%04d", i) + FILE_EXTENSION;
    }
    PixmapIO.writePNG(Gdx.files.external(filePath), pixmap);
    pixmap.dispose();
  }
}
