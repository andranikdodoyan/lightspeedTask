package model;

import sql.Operation;

public class Column {

  private String name;
  private Operation operation;
  private String alias;

  private String table;


  public Column(String name, Operation operation, String alias, String table) {
    this.name = name;
    this.operation = operation;
    this.alias = alias;
    this.table = table;
  }
}
