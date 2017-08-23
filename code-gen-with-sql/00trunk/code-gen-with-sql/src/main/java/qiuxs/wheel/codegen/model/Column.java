package qiuxs.wheel.codegen.model;

public class Column {

	private String name;
	private String jdbcType;
	private String javaType;
	private int[] length;
	private String comment;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if (name.startsWith("`")) {
			name = name.substring(1);
		}
		if (name.endsWith("`")) {
			name = name.substring(0, name.length() - 1);
		}
		this.name = name;
	}

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
		if ("varchar".equalsIgnoreCase(jdbcType)) {
			this.setJavaType("String");
		} else if ("int".equalsIgnoreCase(jdbcType)) {
			this.javaType = "Integer";
		} else if ("bigint".equalsIgnoreCase(jdbcType)) {
			this.javaType = "Long";
		} else if ("datetime".equalsIgnoreCase(jdbcType) || "date".equalsIgnoreCase(jdbcType)
				|| "timestamp".equalsIgnoreCase(jdbcType)) {
			this.javaType = "Date";
		} else if ("decimal".equalsIgnoreCase(jdbcType)) {
			this.javaType = "BigDecimal";
		}
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public int[] getLength() {
		return length;
	}

	public void setLength(int[] length) {
		this.length = length;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		if (comment.startsWith("'")) {
			comment = comment.substring(1);
		}
		if (comment.endsWith("'")) {
			comment = comment.substring(0, comment.length() - 1);
		}
		this.comment = comment;
	}

	public String getImportString() {
		if ("Date".equals(this.javaType)) {
			return "import java.util.Date;";
		} else if ("BigDecimal".equals(this.javaType)) {
			return "import java.math.BigDecimal;";
		}
		return null;
	}
}
