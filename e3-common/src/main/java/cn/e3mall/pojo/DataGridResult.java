package cn.e3mall.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DataGridResult implements Serializable {
	private static final long serialVersionUID = 1L;
private int total;
  private List<?>rows= new ArrayList<>();
public int getTotal() {
	return total;
}
public void setTotal(int total) {
	this.total = total;
}
public List<?> getRows() {
	return rows;
}
public void setRows(List<?> rows) {
	this.rows = rows;
}
}
