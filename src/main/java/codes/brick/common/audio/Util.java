package codes.brick.common.audio;

import java.util.concurrent.TimeUnit;

public class Util {
  public static final float RECENT = TimeUnit.NANOSECONDS.convert(100, TimeUnit.MILLISECONDS);

  private static final int EXP_ADJUSTMENT = 3;
  private static final float MIN_MULTIPLIER = 0.02f;

  /**
   * An exponential curve (rather than a linear curve)
   * matches our ears much better, so we adjust every multiplier by that before we play them.
   *
   * @param volumeMultiplier multiplier to adjust
   * @return Adjusted multiplier
   */
  public static float expAdjustMultiplier(float volumeMultiplier) {
    float multiplier = floatPow(volumeMultiplier, EXP_ADJUSTMENT);
    // Arbitrary minimum; 0.01 is too quiet to hear but is a possible volume from the slider
    if (multiplier < MIN_MULTIPLIER && multiplier > 0) {
      multiplier = MIN_MULTIPLIER;
    }
    return multiplier;
  }

  /**
   * Like pow, but for floats. Does NOT do any sort of overflow handling.
   *
   * @param value Base
   * @param times Exponent
   * @return Result of pow computation
   *
   */
  public static float floatPow(float value, int times) {
    float sum = value;
    for (int i = 1; i < times; i++) {
      sum *= value;
    }
    return sum;
  }
}
