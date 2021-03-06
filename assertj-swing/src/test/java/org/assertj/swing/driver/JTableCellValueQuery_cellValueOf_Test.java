/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * Copyright 2012-2015 the original author or authors.
 */
package org.assertj.swing.driver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.edt.GuiActionRunner.execute;
import static org.assertj.swing.test.core.MethodInvocations.Args.args;

import org.assertj.swing.annotation.RunsInEDT;
import org.assertj.swing.test.core.MethodInvocations;
import org.assertj.swing.test.core.MethodInvocations.Args;
import org.assertj.swing.test.core.RobotBasedTestCase;
import org.assertj.swing.test.swing.TestTable;
import org.assertj.swing.test.swing.TestWindow;
import org.junit.Test;

/**
 * Tests for {@link JTableCellValueQuery#cellValueOf(javax.swing.JTable, int, int)}.
 * 
 * @author Alex Ruiz
 */
public class JTableCellValueQuery_cellValueOf_Test extends RobotBasedTestCase {
  private MyTable table;

  @Override
  protected void onSetUp() {
    MyWindow window = MyWindow.createNew();
    table = window.table;
  }

  @Test
  public void should_Return_JTable_Cell_Value() {
    table.startRecording();
    assertThat(JTableCellValueQuery.cellValueOf(table, 0, 2)).isEqualTo("0-2");
    table.requireInvoked("getValueAt", args(0, 2));
  }

  private static class MyWindow extends TestWindow {
    final MyTable table = new MyTable();

    @RunsInEDT
    static MyWindow createNew() {
      return execute(() -> new MyWindow());
    }

    private MyWindow() {
      super(JTableCellValueQuery_cellValueOf_Test.class);
      addComponents(table);
    }
  }

  private static class MyTable extends TestTable {
    private boolean recording;
    private final MethodInvocations methodInvocations = new MethodInvocations();

    MyTable() {
      super(2, 6);
    }

    @Override
    public Object getValueAt(int row, int column) {
      if (recording) {
        methodInvocations.invoked("getValueAt", args(row, column));
      }
      return super.getValueAt(row, column);
    }

    void startRecording() {
      recording = true;
    }

    MethodInvocations requireInvoked(String methodName, Args args) {
      return methodInvocations.requireInvoked(methodName, args);
    }
  }
}
