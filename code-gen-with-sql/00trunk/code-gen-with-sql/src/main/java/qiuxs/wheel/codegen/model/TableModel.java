package qiuxs.wheel.codegen.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TableModel {

	private String tableName;
	private List<Column> columns = new ArrayList<Column>();
	private Set<String> imports = new HashSet<String>();

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<Column> getColumns() {
		return columns;
	}

	public void setColumns(List<Column> columns) {
		this.columns = columns;
	}

	public Set<String> getImports() {
		return imports;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}

	public void addImport(String importString) {
		if (importString != null)
			this.imports.add(importString);
	}

	public void addColumn(Column col) {
		this.columns.add(col);
		this.addImport(col.getImportString());
	}

}
