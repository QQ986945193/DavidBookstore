package com.qq986945193.davidbookstore.book.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.david.webtools.common.jdbc.TxQueryRunner;
import com.david.webtools.common.utils.CommonUtils;
import com.qq986945193.davidbookstore.book.domain.Book;
import com.qq986945193.davidbookstore.category.domain.Category;

/**
 * 书籍的持久层
 */
public class BookDao {
	private QueryRunner qr = new TxQueryRunner();

	/**
	 * 根据cid查询图书
	 * 
	 * @param cid
	 * @return
	 */
	public List<Book> findBookByCid(String cid) {
		try {
			String sql = "select * from book where cid = ?";
			return qr.query(sql, new BeanListHandler<Book>(Book.class), cid);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 查询所有图书
	 */
	public List<Book> findAllBook() {
		try {
			String sql = "select * from book";
			return qr.query(sql, new BeanListHandler<Book>(Book.class));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	/**
	 * 根据bid查询书籍的详情
	 * 
	 * @param bid
	 * @return
	 */
	public Book loadByBid(String bid) {
		try {
//			String sql = "select * from book where bid = ?";
//			return qr.query(sql, new BeanHandler<Book>(Book.class), bid);
			/**
			 * 因为我们需要在Book对象中保存Category的信息。所以我们不使用上面的代码
			 */
			String sql = "select * from book where bid=?";
			Map<String,Object> map = qr.query(sql, new MapHandler(), bid);
			/**
			 * 使用一个Map，映射出两个对象，再给这两个对象建立关联关系
			 */
			Category category = CommonUtils.toBean(map, Category.class);
			Book book = CommonUtils.toBean(map, Book.class);
			book.setCategory(category);
			return book;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据cid查询当前分类下的图书数量
	 */
	public int getCountByCid(String cid) throws SQLException {
		String sql = "select count(*) from book where cid = ?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), cid);
		return number.intValue();
	}

	/**
	 * 修改图书的信息
	 */
	public void updateBook(Book book) throws SQLException {
		String sql = "update book set bname =? , price = ? , author = ? , image = ? where bid = ?";
		qr.update(sql, book.getBname(), book.getPrice(), book.getAuthor(), book.getImage(), book.getBid());
	}

}
