package model;

import types.JoinType;

public class Join {

  private ColumnTable columnTable1;
  private ColumnTable columnTable2;
  private JoinType joinType;

  public Join(ColumnTable columnTable1, ColumnTable columnTable2, JoinType joinType) {
    this.columnTable1 = columnTable1;
    this.columnTable2 = columnTable2;
    this.joinType = joinType;
  }
}
