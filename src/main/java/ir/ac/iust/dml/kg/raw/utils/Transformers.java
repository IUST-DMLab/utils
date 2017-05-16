package ir.ac.iust.dml.kg.raw.utils;

public class Transformers {

  public Long toDouble(String str) {
    try {
      return Long.parseLong(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Long toLong(String str) {
    try {
      return Long.parseLong(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer toInt(String str) {
    try {
      return Integer.parseInt(str);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer intRangeStart(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Integer.parseInt(splits[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Integer intRangeEnd(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Integer.parseInt(splits[1]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Long longRangeStart(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Long.parseLong(splits[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Long longRangeEnd(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Long.parseLong(splits[1]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Double doubleRangeStart(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Double.parseDouble(splits[0]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Double doubleRangeEnd(String str) {
    try {
      String[] splits = str.split("\\s+-\\s+");
      return Double.parseDouble(splits[1]);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Double latLong(String str) {
    try {
      return Double.parseDouble(str);
    } catch (Exception ignore) {
      String[] splits = str.split("\\s+");
      if (splits.length != 3) return null;
      if (splits[1].contains("'")) return null;
      if (splits[2].contains("\"")) return null;
      try {
        return Integer.parseInt(splits[0])
            + (Integer.parseInt(splits[0]) / 60.0)
            + (Integer.parseInt(splits[0]) / 3600.0);
      } catch (Exception e) {
        e.printStackTrace();
        return null;
      }
    }
  }
}
