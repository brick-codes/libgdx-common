package codes.brick.common;

import java.util.Random;

/**
 * Kind of a misnomer, because Random is always seeded.
 * But this lets us get the seed we put into it.
 */
public class SeededRandom extends Random {
  private final long savedSeed;
  
  public SeededRandom(long seed) {
    super(seed);
    savedSeed = seed;
  }
  
  public long getSeed() {
    return savedSeed;
  }
}
