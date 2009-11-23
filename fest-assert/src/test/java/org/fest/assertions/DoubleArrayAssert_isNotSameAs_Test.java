/*
 * Created on Feb 14, 2008
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright @2008-2009 the original author or authors.
 */
package org.fest.assertions;

import static org.fest.assertions.EmptyArrays.emptyDoubleArray;
import static org.fest.assertions.ArrayFactory.doubleArray;
import static org.fest.test.ExpectedFailure.expectAssertionError;

import org.fest.test.CodeToTest;
import org.junit.*;

/**
 * Tests for <code>{@link DoubleArrayAssert#isNotSameAs(double[])}</code>.
 *
 * @author Yvonne Wang
 * @author Alex Ruiz
 */
public class DoubleArrayAssert_isNotSameAs_Test implements GenericAssert_isNotSameAs_TestCase {

  private static double[] array;

  @BeforeClass
  public static void setUpOnce() {
    array = doubleArray(55.03, 4345.91);
  }

  @Test
  public void should_pass_if_actual_and_expected_are_not_same() {
    new DoubleArrayAssert(array).isNotSameAs(emptyDoubleArray());
  }

  @Test
  public void should_fail_if_actual_and_expected_are_same() {
    expectAssertionError("given objects are same:<[55.03, 4345.91]>").on(new CodeToTest() {
      public void run() {
        new DoubleArrayAssert(array).isNotSameAs(array);
      }
    });
  }

  @Test
  public void should_fail_and_display_description_of_assertion_if_actual_and_expected_are_same() {
    expectAssertionError("[A Test] given objects are same:<[55.03, 4345.91]>").on(new CodeToTest() {
      public void run() {
        new DoubleArrayAssert(array).as("A Test")
                                    .isNotSameAs(array);
      }
    });
  }

  @Test
  public void should_fail_with_custom_message_if_actual_and_expected_are_same() {
    expectAssertionError("My custom message").on(new CodeToTest() {
      public void run() {
        new DoubleArrayAssert(array).overridingErrorMessage("My custom message")
                                    .isNotSameAs(array);
      }
    });
  }

  @Test
  public void should_fail_with_custom_message_ignoring_description_of_assertion_if_actual_and_expected_are_same() {
    expectAssertionError("My custom message").on(new CodeToTest() {
      public void run() {
        new DoubleArrayAssert(array).as("A Test")
                                    .overridingErrorMessage("My custom message")
                                    .isNotSameAs(array);
      }
    });
  }
}