package qiuxs.wheel.codegen;

import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import qiuxs.wheel.codegen.model.Column;
import qiuxs.wheel.codegen.model.TableModel;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws JSQLParserException {

		String sql = "CREATE TABLE `sc_serverip` (" +
				"`id` BIGINT(20) NOT NULL," +
				"`name` VARCHAR(40) DEFAULT ''," +
				"`dianxin` VARCHAR(100) DEFAULT ''," +
				"`rem` VARCHAR(400) DEFAULT ''," +
				"`opid` INT(11) DEFAULT '0'," +
				"`delflag` INT(2) DEFAULT '0'," +
				"`delopid` INT(11) DEFAULT '0'," +
				"`deloptime` DATETIME DEFAULT NULL," +
				"`optime` DATETIME DEFAULT NULL," +
				"`wangtong` VARCHAR(400) DEFAULT ''," +
				"`sparedx` VARCHAR(400) DEFAULT ''," +
				"`sparewt` VARCHAR(400) DEFAULT ''," +
				"`entryid` VARCHAR(50) DEFAULT '' COMMENT '入口实例'," +
				"`flag` INT(3) DEFAULT '0' COMMENT '暂停新增标记'," +
				"`msgserverurl` VARCHAR(100) DEFAULT '' COMMENT '消息服务器地址'," +
				"`socketurl` VARCHAR(100) DEFAULT '' COMMENT '消息服务器地址'," +
				"`entrypartner` VARCHAR(50) DEFAULT '' COMMENT '实例伙伴'," +
				"`systype` INT(11) DEFAULT '2' COMMENT '系统类型:3=零售，2=批发'," +
				"`areacode` VARCHAR(40) DEFAULT '' COMMENT '区域代码'," +
				"`cityid` INT(11) DEFAULT '0' COMMENT 'cityid'," +
				"`upstream` VARCHAR(100) DEFAULT '' COMMENT '后台地址'," +
				"`localoa_url` VARCHAR(100) DEFAULT '' COMMENT '本地OA'," +
				"`sslurl1` VARCHAR(100) DEFAULT '' COMMENT 'ssl地址1'," +
				"`sslurl2` VARCHAR(100) DEFAULT '' COMMENT 'ssl地址2'," +
				"PRIMARY KEY (`id`)" +
				");";
		Statement statement = CCJSqlParserUtil.parse(sql);
		CreateTable createTable = (CreateTable) statement;
		List<ColumnDefinition> columns = createTable.getColumnDefinitions();
		TableModel model = new TableModel();
		model.setTableName(createTable.getTable().getName());
		for (ColumnDefinition cd : columns) {
			Column col = new Column();
			col.setName(cd.getColumnName());
			col.setJdbcType(cd.getColDataType().getDataType());
			List<String> ls_args = cd.getColDataType().getArgumentsStringList();
			if (ls_args != null && ls_args.size() > 0) {
				int[] length = new int[ls_args.size()];
				for (int i = 0; i < ls_args.size(); i++) {
					length[i] = Integer.parseInt(ls_args.get(i));
				}
				col.setLength(length);
			}
			List<String> specString = cd.getColumnSpecStrings();
			if (specString.size() >= 4) {
				for (int i = 0; i < specString.size(); i++) {
					String str = specString.get(i);
					if ("comment".equalsIgnoreCase(str)) {
						col.setComment(specString.get(i + 1));
					}
				}
			}
			model.addColumn(col);
		}
		System.out.println(model);
	}
}
