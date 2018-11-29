package blog;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//Blogを操作するビジネロジック
public class BlogController {
	
	//シングルトンのインスタンス
	private static BlogController controller = new BlogController();
	
	//このクラスのインスタンスを取得する。＠return BlogControllerのインスタンス
	public static BlogController getInstance() {
		return controller;
	}
	private BlogController() {
		
	}
	
	//トピックをポスト（登録）します。＠param topic トピック
	public void postTopic (Topic topic) {
//		String sql = "insert into topic(title,content)"+ "values("+"'"+ topic.getTitle() + "'"
//				+"," + topic.getContent() +"'" + ")";
		//SQL文作成はきちんと確認してください　　　　楊　20181128
		String sql = "insert into topic(title,content)"+ "values('"+ topic.getTitle() + "','"
				+ topic.getContent() +"')";
		//String sql = "DELETE FROM topic where title='こんにちは'";
		
		Connection con = null;
		Statement smt = null;
		try {
			con = ConnectionManager.getConnection();
			smt = con.createStatement();
			System.out.println("楊 20181128 sql=" + sql);
			smt.executeUpdate(sql);
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (smt != null) {
				try {
					smt.close();
				//あえて例外を処理していません
				} catch ( Exception ignore) {
					// TODO: handle exception
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
		}
		
	}
	
	//最近の（というか全部の）トピックを取得する。＠return　トピックのリスト
	public List<Topic> getTopics() {
		String sql = "select * from topic";
		List<Topic> topics = new ArrayList<Topic>();
		
		Connection con = null;
		Statement smt = null;
		//
		ResultSet rs = null;
		try {
			con = ConnectionManager.getConnection();
			smt = con.createStatement();
			rs = smt.executeQuery(sql);
			//ResultSetのnext()メソッドカーソルを現在の位置から 1 行順方向に移動します。
			while (rs.next()) {
				Topic topic = new Topic();
				/*getInt:この ResultSet オブジェクトの現在行にある指定された列の値を、
				Java プログラミング言語のintとして取得します。*/
				topic.setId(rs.getInt("id"));
				/*getTimestamp:この ResultSet オブジェクトの現在行にある指定された列の値を、
				Java プログラミング言語の java.sql.Timestamp オブジェクトとして取得します。*/
				topic.setPostDate(rs.getTimestamp("post_date"));
				/*getString:この ResultSet オブジェクトの現在行にある指定された列の値を、
				Java プログラミング言語の String として取得します。*/
				topic.setContent(rs.getString("content"));
				topic.setTitle(rs.getString("title"));
				topics.add(topic);
			}
		//JDBCを使用してSQLを使用した際に問題が発生する例外
		} catch (SQLException e) {
			// TODO: handle exception
		e.printStackTrace();
		}finally {
			if (rs != null) {
				try {
					rs.close();
				} catch ( Exception ignore) {
					// TODO: handle exception
				}
			}
			if (smt != null) {
				try {
					smt.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
			
			if (con != null) {
				try {
					con.close();
				} catch (Exception ignore) {
					// TODO: handle exception
				}
			}
		}
		return topics;
	}
	public static void main(String[] args) {
		BlogController ctrl = BlogController.getInstance();
		List<Topic> topics = ctrl.getTopics();
		for (int i=0; i< topics.size(); i++) {
			System.out.println(topics.get(i));
	}
		System.out.println("end");
	}
}
