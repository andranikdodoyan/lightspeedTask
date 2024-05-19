package model;

import types.SourceType;

public class Source {
  String table;
  String alias;

  SourceType type;

  public Source(String table, String alias, SourceType type) {
    this.table = table;
    this.alias = alias;
    this.type = type;
  }
}
