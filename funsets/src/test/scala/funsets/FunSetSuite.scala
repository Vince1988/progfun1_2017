package funsets

import org.scalatest.FunSuite


import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

/**
 * This class is a test suite for the methods in object FunSets. To run
 * the test suite, you can either:
 *  - run the "test" command in the SBT console
 *  - right-click the file in eclipse and chose "Run As" - "JUnit Test"
 */
@RunWith(classOf[JUnitRunner])
class FunSetSuite extends FunSuite {

  /**
   * Link to the scaladoc - very clear and detailed tutorial of FunSuite
   *
   * http://doc.scalatest.org/1.9.1/index.html#org.scalatest.FunSuite
   *
   * Operators
   *  - test
   *  - ignore
   *  - pending
   */

  /**
   * Tests are written using the "test" operator and the "assert" method.
   */
  // test("string take") {
  //   val message = "hello, world"
  //   assert(message.take(5) == "hello")
  // }

  /**
   * For ScalaTest tests, there exists a special equality operator "===" that
   * can be used inside "assert". If the assertion fails, the two values will
   * be printed in the error message. Otherwise, when using "==", the test
   * error message will only say "assertion failed", without showing the values.
   *
   * Try it out! Change the values so that the assertion fails, and look at the
   * error message.
   */
  // test("adding ints") {
  //   assert(1 + 2 === 3)
  // }


  import FunSets._

  test("contains is implemented") {
    assert(contains(x => true, 100))
  }

  /**
   * When writing tests, one would often like to re-use certain values for multiple
   * tests. For instance, we would like to create an Int-set and have multiple test
   * about it.
   *
   * Instead of copy-pasting the code for creating the set into every test, we can
   * store it in the test class using a val:
   *
   *   val s1 = singletonSet(1)
   *
   * However, what happens if the method "singletonSet" has a bug and crashes? Then
   * the test methods are not even executed, because creating an instance of the
   * test class fails!
   *
   * Therefore, we put the shared values into a separate trait (traits are like
   * abstract classes), and create an instance inside each test method.
   *
   */

  trait TestSets {
    val s1: Set = singletonSet(1)
    val s2: Set = singletonSet(2)
    val s3: Set = singletonSet(3)
    val s4: Set = x => x >= 0 && x < 10
  }

  /**
   * This test is currently disabled (by using "ignore") because the method
   * "singletonSet" is not yet implemented and the test would fail.
   *
   * Once you finish your implementation of "singletonSet", exchange the
   * function "ignore" by "test".
   */
  test("singletonSet(1) contains 1") {

    /**
     * We create a new instance of the "TestSets" trait, this gives us access
     * to the values "s1" to "s3".
     */
    new TestSets {
      /**
       * The string argument of "assert" is a message that is printed in case
       * the test fails. This helps identifying which assertion failed.
       */
      assert(contains(s1, 1), "Singleton")
    }
  }

  test("union contains all elements of each set") {
    new TestSets {
      val s = union(s1, s2)
      assert(contains(s, 1), "Union 1")
      assert(contains(s, 2), "Union 2")
      assert(!contains(s, 3), "Union 3")
    }
  }

  test("intersect returns the intersection of two given sets") {
    new TestSets {
      val u1 = union(s1, s2)
      val u2 = union(s2, s3)
      val s = intersect(u1, u2)

      assert(contains(s, 2), "Intersect 1")
      assert(!contains(s, 3), "Intersect 2")
      assert(!contains(s, 1), "Intersect 3")
    }
  }

  test("diff returns the difference of two given sets") {
    new TestSets {
      val t: Set = x => x % 2 == 0
      val d: Set = diff(s4, t)

      List(1, 3, 5, 7, 9) foreach (x => assert(contains(d, x), "Diff contains"))
      List(0, 2, 4, 6, 8) foreach (x => assert(!contains(d, x), "Diff !contains"))
    }
  }

  test("filter returns a subset matching a filter function") {
    new TestSets {
      val f: Int => Boolean = _ % 4 == 0
      val s: Set = filter(s4, f)

      List(0, 4, 8) foreach (x => assert(contains(s, x), "Filter contains"))
      List(1, 2, 3, 5, 6, 7, 9) foreach (x => assert(!contains(s, x), "Filter !contains"))
    }
  }

  test("forall returns whether all bounded integers within `s` satisfy `p`") {
    new TestSets {
      val s: Set = _ % 4 == 0

      assert(forall(s, _ % 2 == 0), "Forall 1")
      assert(!forall(s, _ % 3 == 0), "Forall 2")
      assert(!forall(s4, _ % 2 == 0), "Forall 3")
    }
  }

  test("exists returns whether there exists a bounded integer within `s` that satisfies `p`") {
    new TestSets {
      assert(exists(s4, _ == 5), "Exists 1")
      assert(exists(s4, _ % 2 == 0), "Exists 2")
      assert(!exists(s4, _ > 20), "Exists 3")
    }
  }

  test("map returns a set transformed by applying `f` to each element of `s`") {
    new TestSets {
      val m: Set = map(s4, _ + 100)

      assert(contains(map(s1, _ * 2), 2), "Map 1")
      assert(!contains(map(s2, _ * 4), 2), "Map 2")
    }
  }

}
