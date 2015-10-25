package com.intuit.idea.chopsticks.query.copies;

import com.intuit.idea.app.globals.Literals;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * [TODO] finish mysqlQueryBuilder. Right now is not updated to be compatible
 * with comparison engine MySQL query builder
 */
public class MySQLQueryBuilder extends QueryBuilder {

	private final static Logger log = LoggerFactory
			.getLogger(MySQLQueryBuilder.class);

	private final String schema;

	private MySQLQueryBuilder(Builder<?> builder) throws Exception {
		super(builder);
		if (builder.schema == null && !testType.equals(Literals.ADHOC)) {
			log.warn("MySQL table has no specified database.");
			this.schema = "";
			setFrom(table.getName());
		}
		// throw new
		// Exception("Cannot create MYSQL query because there is no specified database.");
		else {
			this.schema = builder.schema;
			setFrom(schema + "." + table.getName());
		}
		this.dateFormatForQuery = Literals.DATE_FORMAT_MYSQL;
		this.dateTimeFormat = DateTimeFormat.forPattern(dateFormatForQuery);
	}

	public static Builder<?> builder() {
		return new Builder2();
	}

	/**
	 * Generates the data query based on test type
	 *
	 * @return String for the data query for the specified database
	 */

	public String generateDataQuery() {
		StringBuilder query = new StringBuilder("SELECT ");
		query.append(dataSelect("%s"));
		query.append(" FROM ");
		query.append(getFrom());

		if (!getWhere().isEmpty())
			query.append(" WHERE ").append(getWhere());

		if (getOrderBy() != null)
			if (hasPks())
				query.append(" ORDER BY ").append(getOrderBy());
		if (fetchAmount > 0) {
			query.append(" LIMIT ").append(fetchAmount);
		}

		return query.toString();
	}

	/**
	 * depending on type of database (database or schemaless) we will generate
	 * the proper source from clause
	 *
	 * @return clause
	 */
	protected String getFrom() {
		if (schema.isEmpty()) {
			return table.getName();
		} else {
			return schema + "." + table.getName();
		}
	}

	@Override
	protected String getDateRangeString(DateTime start, DateTime end) {
		if (start == null && end == null)
			return null; // case shouldn't happen. just in case...

		if (end == null) {
			String startStr = dateTimeFormat.print(start);
			return String
					.format("%s >= %s", getQueryableDateColumn(), startStr);
		}
		if (start == null) {
			String endStr = dateTimeFormat.print(end);
			return String.format("%s <= %s", getQueryableDateColumn(), endStr);
		}

		String startStr = dateTimeFormat.print(start);
		String endStr = dateTimeFormat.print(end);
		return String.format("%s between '%s' and '%s'",
				getQueryableDateColumn(), startStr, endStr);
	}

	public static abstract class Builder<T extends Builder<T>> extends
			QueryBuilder.Builder<T> {
		private String schema;

		public T schema(String schema) {
			this.schema = schema;
			return self();
		}

		public String getQuery() throws Exception {
			MySQLQueryBuilder query = new MySQLQueryBuilder(this);
			return query.generateDataQuery();
		}

		@Override
		public MySQLQueryBuilder build() throws Exception {
			return new MySQLQueryBuilder(this);
		}

	}

	private static class Builder2 extends Builder<Builder2> {
		@Override
		protected Builder2 self() {
			return this;
		}
	}

}
