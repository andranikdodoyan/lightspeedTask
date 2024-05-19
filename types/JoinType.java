package types;

public enum JoinType {
  LEFT_JOIN("LEFT JOIN"),
  RIGHT_JOIN("RIGHT JOIN"),
  INNER_JOIN("INNER JOIN"),
  FULL_JOIN("FULL JOIN");

  private final String value;

  JoinType(String v) {
    this.value = v;
  }

  public String getValue() {
    return value;
  }
}
